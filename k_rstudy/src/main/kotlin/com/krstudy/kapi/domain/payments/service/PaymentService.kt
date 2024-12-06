package com.krstudy.kapi.domain.payments.service

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.domain.member.service.MemberService
import com.krstudy.kapi.domain.payments.dto.*
import com.krstudy.kapi.domain.payments.entity.Payment
import com.krstudy.kapi.domain.payments.repository.PaymentRepository
import com.krstudy.kapi.domain.payments.status.PaymentStatus
import net.minidev.json.JSONObject
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.LocalDateTime
import java.util.Base64
import com.krstudy.kapi.global.exception.GlobalException
import com.krstudy.kapi.global.exception.MessageCode

@Service
@Transactional
class PaymentService(
    private val paymentRepository: PaymentRepository,
    private val memberService: MemberService,
    @Value("\${api.key}") private val secretKey: String
) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val tossPaymentUrl = "https://api.tosspayments.com/v1/payments/confirm"

    fun confirmPayment(request: PaymentRequestDto, author: Member?): PaymentResponseDto {
        try {

            // 2. 토스 페이먼츠 API 호출
            val tossResponse = callTossPaymentApi(request)

            // 3. 결제 정보 저장
            val payment = Payment(
                orderId = request.orderId,
                amount = request.amount,
                paymentKey = request.paymentKey,
                status = PaymentStatus.COMPLETED,
                member = author,
                completedAt = LocalDateTime.now()
            )

            val savedPayment = paymentRepository.save(payment)
            return PaymentResponseDto(
                orderId = savedPayment.orderId,
                amount = savedPayment.amount,
                status = savedPayment.status,
                paymentKey = savedPayment.paymentKey,
                completedAt = savedPayment.completedAt
            )
        } catch (e: Exception) {
            logger.error("Payment processing error", e)
            when (e) {
                is GlobalException -> throw e
                else -> {
                    // 실패한 결제 정보 저장
                    saveFailedPayment(request, author)
                    throw GlobalException(MessageCode.PAYMENT_PROCESSING_ERROR)
                }
            }
        }
    }

    private fun saveFailedPayment(request: PaymentRequestDto, author: Member?) {
        try {

            val failedPayment = Payment(
                orderId = request.orderId,
                amount = request.amount,
                paymentKey = request.paymentKey,
                status = PaymentStatus.FAILED,
                member = author,
                completedAt = LocalDateTime.now()
            )
            paymentRepository.save(failedPayment)
        } catch (e: Exception) {
            logger.error("Failed to save failed payment", e)
        }
    }

    private fun callTossPaymentApi(request: PaymentRequestDto): TossPaymentResponse {
        val client = HttpClient.newBuilder().build()

        val requestBody = JSONObject().apply {
            put("paymentKey", request.paymentKey)
            put("orderId", request.orderId)
            put("amount", request.amount)
        }

        logger.debug("Sending request to Toss API: $requestBody")

        val httpRequest = HttpRequest.newBuilder()
            .uri(URI(tossPaymentUrl))
            .header("Authorization", "Basic ${Base64.getEncoder().encodeToString("$secretKey:".toByteArray())}")
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
            .build()

        return try {
            val response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString())
            logger.debug("Toss API Response - Status: ${response.statusCode()}, Body: ${response.body()}")

            when (response.statusCode()) {
                200 -> {
                    val mapper = ObjectMapper().apply {
                        // JSON 처리 시 알 수 없는 속성 무시
                        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                        // null 값을 기본값으로 처리
                        configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
                        // 빈 객체 허용
                        configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false)
                    }
                    mapper.readValue(response.body(), TossPaymentResponse::class.java)
                }
                401 -> throw GlobalException(MessageCode.UNAUTHORIZED)
                else -> {
                    logger.error("Toss payment failed with status: ${response.statusCode()}, body: ${response.body()}")
                    throw GlobalException(MessageCode.PAYMENT_FAILED)
                }
            }
        } catch (e: Exception) {
            logger.error("Toss API call failed", e)
            when (e) {
                is GlobalException -> throw e
                else -> throw GlobalException(MessageCode.PAYMENT_PROCESSING_ERROR)
            }
        }
    }

    // 결제 조회
    @Transactional(readOnly = true)
    fun getPaymentByOrderId(orderId: String): Payment? {
        return paymentRepository.findByOrderId(orderId)
    }

    // 사용자의 결제 내역 조회
    @Transactional(readOnly = true)
    fun getPaymentsByMember(memberId: Long): List<Payment> {
        return paymentRepository.findByMemberId(memberId)
    }
}