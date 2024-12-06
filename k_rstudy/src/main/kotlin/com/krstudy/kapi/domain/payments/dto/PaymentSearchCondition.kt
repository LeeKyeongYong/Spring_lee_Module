package com.krstudy.kapi.domain.payments.dto

import com.krstudy.kapi.domain.payments.status.PaymentStatus
import java.time.LocalDateTime

data class PaymentSearchCondition(
    val startDate: LocalDateTime? = null,
    val endDate: LocalDateTime? = null,
    val status: PaymentStatus? = null,
    val memberName: String? = null,
    val orderId: String? = null
)