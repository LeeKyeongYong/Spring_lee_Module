package com.krstudy.kapi.domain.payments.controller

import com.krstudy.kapi.global.https.ReqData
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/payments")
class PaymentController (private val rq: ReqData) {

    @Value("\${api.clientKey}")
    private lateinit var apiKey: String

    @GetMapping("/checkout")
    fun index(request: HttpServletRequest): String {
        rq.setAttribute("apiKey", apiKey)
        return "domain/payments/checkout"
    }

    @GetMapping("/success")
    fun paymentRequest(        @RequestParam paymentKey: String,
                               @RequestParam orderId: String,
                               @RequestParam amount: String): String {
        rq.setAttribute("paymentKey", paymentKey)
        rq.setAttribute("orderId", orderId)
        rq.setAttribute("amount", amount)
        return "domain/payments/success"
    }

    @GetMapping("/fail")
    fun failPayment( @RequestParam code: String,
                     @RequestParam message: String): String {
        rq.setAttribute("code", (code?: "undefined"))
        rq.setAttribute("message", (message?: "알 수 없는 오류가 발생했습니다."))
        return "domain/payments/fail"
    }
}