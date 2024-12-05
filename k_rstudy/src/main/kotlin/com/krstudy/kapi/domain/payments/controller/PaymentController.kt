package com.krstudy.kapi.domain.payments.controller

import com.krstudy.kapi.domain.payments.service.PaymentService
import com.krstudy.kapi.global.Security.SecurityUtil
import com.krstudy.kapi.global.exception.GlobalException
import com.krstudy.kapi.global.exception.MessageCode
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RestController
@RequestMapping("/payments")
class PaymentController(
    private val paymentService: PaymentService,
    private val securityUtil: SecurityUtil
) {

    @PostMapping("/confirm")
    @PreAuthorize("isAuthenticated()")  // 인증된 사용자만 접근 가능
    fun confirmPayment(
        @RequestParam paymentKey: String,
        @RequestParam orderId: String,
        @RequestParam amount: BigDecimal
    ): ResponseEntity<String> {
        // 현재 로그인한 사용자의 ID 가져오기
        val currentUserId = securityUtil.getCurrentUserId()
            ?: throw GlobalException(MessageCode.UNAUTHORIZED_LOGIN_REQUIRED)

        return paymentService.processPayment(
            paymentKey = paymentKey,
            orderId = orderId,
            amount = amount,
            memberUserId = currentUserId
        ).fold(
            { error -> ResponseEntity.badRequest().body(error.message) },
            { ResponseEntity.ok("결제 성공") }
        )
    }
}