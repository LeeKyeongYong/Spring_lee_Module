package com.krstudy.kapi.domain.payments.controller

import com.krstudy.kapi.domain.payments.dto.PaymentRequest
import com.krstudy.kapi.domain.payments.dto.PaymentResponse
import com.krstudy.kapi.domain.payments.service.IdempotencyService
import com.krstudy.kapi.domain.payments.service.PaymentService
import com.krstudy.kapi.global.Security.SecurityUtil
import com.krstudy.kapi.global.exception.GlobalException
import com.krstudy.kapi.global.exception.MessageCode
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.net.HttpURLConnection
import java.net.URL
import java.util.Base64
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter

@RestController
@RequestMapping("/api/v1/payments")
class ApiPaymentController(
    private val paymentService: PaymentService,
    private val securityUtil: SecurityUtil,
    private val idempotencyService: IdempotencyService
) {
    @Value("\${api.key}")
    private lateinit var apiKey: String

    @PostMapping("/confirm")
    @PreAuthorize("isAuthenticated()")
    fun confirmPayment(
        @RequestBody paymentRequest: PaymentRequest  // RequestBody로 변경
    ): ResponseEntity<PaymentResponse> {
        val currentUserId = securityUtil.getCurrentUserId()
            ?: throw GlobalException(MessageCode.UNAUTHORIZED_LOGIN_REQUIRED)

        return idempotencyService.processWithIdempotency(
            idempotencyKey = paymentRequest.paymentKey,
            path = "/api/v1/payments/confirm",
            method = "POST"
        ) {
            try {
                val url = URL("https://api.tosspayments.com/v1/payments/confirm")
                val connection = url.openConnection() as HttpURLConnection
                val authorization = "Basic " + Base64.getEncoder().encodeToString("$apiKey:".toByteArray())

                with(connection) {
                    requestMethod = "POST"
                    doOutput = true
                    setRequestProperty("Content-Type", "application/json")
                    setRequestProperty("Authorization", authorization)
                }

                // JSON 요청 본문 생성
                val jsonRequest = JSONObject().apply {
                    put("paymentKey", paymentRequest.paymentKey)
                    put("orderId", paymentRequest.orderId)
                    put("amount", paymentRequest.amount)
                }

                // 요청 전송
                OutputStreamWriter(connection.outputStream).use { writer ->
                    writer.write(jsonRequest.toString())
                    writer.flush()
                }

                // 응답 처리
                val responseCode = connection.responseCode
                val responseBody = BufferedReader(
                    InputStreamReader(
                        if (responseCode == 200) connection.inputStream
                        else connection.errorStream
                    )
                ).use { it.readText() }

                if (responseCode == 200) {
                    paymentService.processPayment(
                        paymentKey = paymentRequest.paymentKey,
                        orderId = paymentRequest.orderId,
                        amount = paymentRequest.amount,
                        memberUserId = currentUserId
                    )
                    ResponseEntity.ok(
                        PaymentResponse(
                            success = true,
                            message = "결제가 성공적으로 처리되었습니다.",
                            orderId = paymentRequest.orderId,
                            amount = paymentRequest.amount
                        )
                    )
                } else {
                    throw GlobalException("400-1", "결제 승인 실패: $responseBody")
                }
            } catch (e: Exception) {
                throw GlobalException("500-1", "결제 처리 중 오류 발생: ${e.message}")
            }
        }
    }

    @GetMapping("/history")
    @PreAuthorize("isAuthenticated()")
    fun getPaymentHistory(): ResponseEntity<List<PaymentResponse>> {
        val currentUserId = securityUtil.getCurrentUserId()
            ?: throw GlobalException(MessageCode.UNAUTHORIZED_LOGIN_REQUIRED)

        return try {
            val paymentHistory = paymentService.getPaymentHistory(currentUserId)
            ResponseEntity.ok(paymentHistory)
        } catch (e: GlobalException) {
            ResponseEntity.badRequest().body(
                listOf(PaymentResponse(success = false, message = e.rsData.msg))
            )
        }
    }
}