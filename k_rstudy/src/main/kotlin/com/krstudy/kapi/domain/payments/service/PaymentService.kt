package com.krstudy.kapi.domain.payments.service

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.krstudy.kapi.domain.payments.client.TossPaymentClient
import com.krstudy.kapi.domain.payments.entity.Order
import com.krstudy.kapi.domain.payments.entity.Payment
import com.krstudy.kapi.domain.payments.repository.OrderRepository
import com.krstudy.kapi.domain.payments.repository.PaymentRepository
import com.krstudy.kapi.global.exception.GlobalException
import com.krstudy.kapi.global.exception.MessageCode
import com.krstudy.kapi.global.exception.PaymentError
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class PaymentService(
    private val paymentRepository: PaymentRepository,
    private val orderRepository: OrderRepository,
    private val tossPaymentClient: TossPaymentClient
) {
    @Transactional
    fun processPayment(
        paymentKey: String,
        orderId: String,
        amount: BigDecimal
    ): Either<PaymentError, Payment> {
        return Either.catch {
            // 1. 결제 금액 검증
            validatePaymentAmount(orderId, amount)
                .fold(
                    { error ->
                        when (error) {
                            is PaymentError.OrderNotFound ->
                                throw GlobalException(MessageCode.NOT_FOUND_RESOURCE)
                            is PaymentError.InvalidAmount ->
                                throw GlobalException("400-9", "결제 금액이 일치하지 않습니다.")
                            is PaymentError.ProcessingError ->
                                throw GlobalException(MessageCode.INTERNAL_SERVER_ERROR)
                        }
                    },
                    { validOrder ->
                        // 검증 성공 시 주문 상태 업데이트
                        validOrder.markAsPaymentPending()
                        orderRepository.save(validOrder)
                    }
                )

            // 2. 토스페이먼츠 API 호출
            val tossPaymentResult = try {
                tossPaymentClient.confirmPayment(
                    paymentKey = paymentKey,
                    orderId = orderId,
                    amount = amount
                )
            } catch (e: Exception) {
                throw GlobalException("500-3", "결제 처리 중 오류가 발생했습니다.")
            }

            // 3. 결제 정보 저장
            val payment = Payment.create(
                orderId = orderId,
                amount = amount,
                paymentKey = paymentKey,
                customerKey = tossPaymentResult.customerKey,
                customerName = tossPaymentResult.customerName,
                customerEmail = tossPaymentResult.customerEmail
            )

            paymentRepository.save(payment).also {
                it.complete()
                // 주문 상태도 함께 업데이트
                orderRepository.findByOrderId(orderId)?.let { order ->
                    order.markAsPaymentCompleted()
                    orderRepository.save(order)
                }
            }
        }.mapLeft { error ->
            when (error) {
                is GlobalException -> PaymentError.ProcessingError(error.rsData.msg)
                else -> PaymentError.ProcessingError(error.message ?: "결제 처리 중 오류가 발생했습니다.")
            }
        }
    }

    private fun validatePaymentAmount(
        orderId: String,
        amount: BigDecimal
    ): Either<PaymentError, Order> {
        val order = orderRepository.findByOrderId(orderId)
            ?: return PaymentError.OrderNotFound("주문을 찾을 수 없습니다.").left()

        return if (order.amount == amount) {
            order.right()
        } else {
            PaymentError.InvalidAmount(
                "결제 금액이 일치하지 않습니다. 주문금액: ${order.amount}, 요청금액: $amount"
            ).left()
        }
    }
}