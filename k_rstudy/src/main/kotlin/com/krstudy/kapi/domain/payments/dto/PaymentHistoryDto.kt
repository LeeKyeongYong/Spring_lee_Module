package com.krstudy.kapi.domain.payments.dto

import com.krstudy.kapi.domain.payments.status.PaymentStatus
import java.math.BigDecimal

data class PaymentHistoryDto(
    val paymentId: Long,
    val orderId: String,
    val amount: BigDecimal,
    val status: PaymentStatus,
    val processedBy: String,
    val processedAt: String,
    val lastModifiedBy: String,
    val lastModifiedAt: String
)