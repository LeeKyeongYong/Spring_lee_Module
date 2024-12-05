package com.krstudy.kapi.domain.payments.controller

import com.krstudy.kapi.domain.payments.dto.PaymentResponse
import com.krstudy.kapi.domain.payments.service.PaymentService
import com.krstudy.kapi.global.Security.SecurityUtil
import com.krstudy.kapi.global.exception.GlobalException
import com.krstudy.kapi.global.exception.MessageCode
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

//개선후버전
@RestController
@RequestMapping("/payments")
class PaymentController(
    private val paymentService: PaymentService,
    private val securityUtil: SecurityUtil
) {

    @PostMapping("/confirm")
    @PreAuthorize("isAuthenticated()")
    fun confirmPayment(
        @RequestParam paymentKey: String,
        @RequestParam orderId: String,
        @RequestParam amount: BigDecimal
    ): ResponseEntity<PaymentResponse> {
        val currentUserId = securityUtil.getCurrentUserId()
            ?: throw GlobalException(MessageCode.UNAUTHORIZED_LOGIN_REQUIRED)

        return paymentService.processPayment(
            paymentKey = paymentKey,
            orderId = orderId,
            amount = amount,
            memberUserId = currentUserId
        ).fold(
            { error ->
                ResponseEntity.badRequest().body(
                    PaymentResponse(
                        success = false,
                        message = error.message
                    )
                )
            },
            { payment ->
                ResponseEntity.ok(
                    PaymentResponse(
                        success = true,
                        message = "결제가 성공적으로 처리되었습니다.",
                        orderId = payment.orderId,
                        amount = payment.amount
                    )
                )
            }
        )
    }

    // 결제 내역 조회 API 추가
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
                listOf(
                    PaymentResponse(
                        success = false,
                        message = e.rsData.msg
                    )
                )
            )
        }
    }
}



// 개선전 버전..
//@RestController
//@RequestMapping("/payments")
//class PaymentController(
//    private val paymentService: PaymentService,
//    private val securityUtil: SecurityUtil
//) {
//
//    @PostMapping("/confirm")
//    @PreAuthorize("isAuthenticated()")  // 인증된 사용자만 접근 가능
//    fun confirmPayment(
//        @RequestParam paymentKey: String,
//        @RequestParam orderId: String,
//        @RequestParam amount: BigDecimal
//    ): ResponseEntity<String> {
//        // 현재 로그인한 사용자의 ID 가져오기
//        val currentUserId = securityUtil.getCurrentUserId()
//            ?: throw GlobalException(MessageCode.UNAUTHORIZED_LOGIN_REQUIRED)
//
//        return paymentService.processPayment(
//            paymentKey = paymentKey,
//            orderId = orderId,
//            amount = amount,
//            memberUserId = currentUserId
//        ).fold(
//            { error -> ResponseEntity.badRequest().body(error.message) },
//            { ResponseEntity.ok("결제 성공") }
//        )
//    }
//}