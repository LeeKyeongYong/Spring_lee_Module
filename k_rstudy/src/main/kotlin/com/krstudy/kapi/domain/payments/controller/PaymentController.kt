package com.krstudy.kapi.domain.payments.controller

import com.krstudy.kapi.domain.payments.service.PaymentService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RestController
@RequestMapping("/payments")
class PaymentController(
    private val paymentService: PaymentService
) {

    @PostMapping("/confirm")
    fun confirmPayment(
        @RequestParam paymentKey: String,
        @RequestParam orderId: String,
        @RequestParam amount: BigDecimal
    ): ResponseEntity<String> {
        return paymentService.processPayment(paymentKey, orderId, amount)
            .fold(
                { error -> ResponseEntity.badRequest().body(error.message) },
                { ResponseEntity.ok("결제 성공") }
            )
    }
}