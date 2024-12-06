package com.krstudy.kapi.domain.payments.controller

import com.krstudy.kapi.domain.payments.dto.PaymentRequestDto
import com.krstudy.kapi.domain.payments.dto.PaymentResponseDto
import com.krstudy.kapi.domain.payments.service.PaymentService
import com.krstudy.kapi.global.exception.GlobalException
import com.krstudy.kapi.global.exception.MessageCode
import com.krstudy.kapi.global.https.ReqData
import org.springframework.security.core.Authentication
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/payments")
class PaymentApiController(
    private val paymentService: PaymentService,
    private val rq: ReqData
) {
    @PostMapping("/confirm")
    fun confirmPayment(
        @RequestBody request: PaymentRequestDto
    ): ResponseEntity<PaymentResponseDto> {
        val member = rq.getMember() ?: throw GlobalException(MessageCode.UNAUTHORIZED)
        val response = paymentService.confirmPayment(request, member)
        return ResponseEntity.ok(response)
    }
}