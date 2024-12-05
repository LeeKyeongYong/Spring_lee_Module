package com.krstudy.kapi.domain.payments.controller

import com.krstudy.kapi.global.https.ReqData
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

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
    fun paymentRequest(request: HttpServletRequest): String {
        return "domain/payments/success"
    }

    @GetMapping("/fail")
    fun failPayment(request: HttpServletRequest): String {
        val failCode = request.getParameter("code")
        val failMessage = request.getParameter("message")

        rq.setAttribute("code", failCode)
        rq.setAttribute("message", failMessage)

        return "domain/payments/fail"
    }
}