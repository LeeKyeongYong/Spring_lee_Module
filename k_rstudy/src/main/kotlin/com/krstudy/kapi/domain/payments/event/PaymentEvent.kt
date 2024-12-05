package com.krstudy.kapi.domain.payments.event

import java.math.BigDecimal

sealed class PaymentEvent {
    data class PaymentCompleted(
        val paymentId: Long,
        val orderId: String,
        val amount: BigDecimal,
        val memberUserId: String
    ) : PaymentEvent()

    data class PaymentFailed(
        val orderId: String,
        val amount: BigDecimal,
        val memberUserId: String,
        val reason: String
    ) : PaymentEvent()
}