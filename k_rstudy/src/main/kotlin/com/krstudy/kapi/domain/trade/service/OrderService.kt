package com.krstudy.kapi.domain.trade.service

import com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.domain.trade.constant.OrderType
import com.krstudy.kapi.domain.trade.dto.OrderRequest
import com.krstudy.kapi.domain.trade.dto.OrderResult
import com.krstudy.kapi.domain.trade.dto.OrderStatus
import com.krstudy.kapi.domain.trade.entity.CoinOrder
import com.krstudy.kapi.domain.trade.event.OrderEvent
import com.krstudy.kapi.domain.trade.repository.OrderRepository
import com.krstudy.kapi.global.exception.GlobalException
import com.krstudy.kapi.global.exception.MessageCode
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val balanceService: BalanceService,
    private val kafkaTemplate: KafkaTemplate<String, OrderEvent>
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    // 매수 주문 처리
    suspend fun processBuyOrder(order: OrderRequest, member: Member): OrderResult {
        try {
            // 1. 잔고 검증
            balanceService.validateAndLockBalance(
                userId = member.userid,
                amount = order.price.multiply(order.quantity)
            )

            // 2. 주문 생성
            val savedOrder = orderRepository.save(
                CoinOrder(
                    userId = member.userid,
                    coinCode = order.coinCode,
                    price = order.price,
                    quantity = order.quantity,
                    type = OrderType.BUY,
                    status = OrderStatus.PENDING
                )
            )

            // 3. 주문 이벤트 발행
            publishOrderEvent(savedOrder)

            return OrderResult(savedOrder.id.toString(), OrderStatus.PENDING)
        } catch (e: Exception) {
            // 4. 실패시 잔고 롤백
            balanceService.unlockBalance(member.userid)
            logger.error("Order processing failed: ${e.message}", e)
            throw GlobalException(MessageCode.PAYMENT_FAILED)
        }
    }

    private fun publishOrderEvent(order: CoinOrder) {
        try {
            kafkaTemplate.send(
                "order-events",
                OrderEvent(
                    orderId = order.id!!,
                    type = order.type,
                    userId = order.userId,
                    coinCode = order.coinCode,
                    price = order.price,
                    quantity = order.quantity
                )
            )
        } catch (e: Exception) {
            logger.error("Failed to publish order event: ${e.message}", e)
            throw GlobalException(MessageCode.PAYMENT_PROCESSING_ERROR)
        }
    }
    // 매도 주문 처리
    suspend fun processSellOrder(order: OrderRequest, member: Member): OrderResult {
        try {
            // 1. 코인 보유량 검증
            balanceService.validateAndLockCoinBalance(
                userId = member.userid,
                coinCode = order.coinCode,
                amount = order.quantity
            )

            // 2. 주문 생성
            val savedOrder = orderRepository.save(
                CoinOrder(
                    userId = member.userid,
                    coinCode = order.coinCode,
                    price = order.price,
                    quantity = order.quantity,
                    type = OrderType.SELL,
                    status = OrderStatus.PENDING
                )
            )

            // 3. 주문 이벤트 발행
            publishOrderEvent(savedOrder)

            return OrderResult(savedOrder.id.toString(), OrderStatus.PENDING)
        } catch (e: Exception) {
            // 4. 실패시 코인 잔고 롤백
            balanceService.unlockCoinBalance(member.userid, order.coinCode)
            logger.error("Sell order processing failed: ${e.message}", e)
            throw GlobalException(MessageCode.PAYMENT_FAILED)
        }
    }
}