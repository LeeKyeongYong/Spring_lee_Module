package com.krstudy.kapi.domain.payments.controller

import com.krstudy.kapi.domain.payments.dto.PaymentCancelRequestDto
import com.krstudy.kapi.domain.payments.dto.PaymentCancelResponseDto
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

    @PostMapping("/{paymentKey}/cancel")
    fun cancelPayment(
        @PathVariable paymentKey: String,
        @RequestBody request: PaymentCancelRequestDto
    ): ResponseEntity<PaymentCancelResponseDto> {
        val member = rq.getMember() ?: throw GlobalException(MessageCode.UNAUTHORIZED)

        // paymentKey를 URL에서 가져와서 request에 설정
        val updatedRequest = request.copy(paymentKey = paymentKey)

        // 결제 건에 대한 소유권 확인
        val payment = paymentService.getPaymentByPaymentKey(paymentKey)
            ?: throw GlobalException(MessageCode.PAYMENT_NOT_FOUND)

        if (payment.member?.id != member.id) {
            throw GlobalException(MessageCode.UNAUTHORIZED)
        }

        val response = paymentService.cancelPayment(updatedRequest)
        return ResponseEntity.ok(response)
    }

}