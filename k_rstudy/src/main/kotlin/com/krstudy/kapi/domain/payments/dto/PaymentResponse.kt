package com.krstudy.kapi.domain.payments.dto

import java.math.BigDecimal
import java.time.LocalDateTime

data class PaymentResponse(
    val success: Boolean,
    val message: String,
    val orderId: String? = null,
    val amount: BigDecimal? = null,
    val paymentDate: LocalDateTime? = null,
    val paymentStatus: String? = null,
    val code: String? = null
)