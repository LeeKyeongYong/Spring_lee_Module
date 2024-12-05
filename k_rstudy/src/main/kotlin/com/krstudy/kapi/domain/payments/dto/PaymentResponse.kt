package com.krstudy.kapi.domain.payments.dto

import com.krstudy.kapi.domain.payments.entity.Payment
import java.math.BigDecimal
import java.time.LocalDateTime

data class PaymentResponse(
    val success: Boolean,
    val message: String,
    val orderId: String? = null,
    val amount: BigDecimal? = null,
    val paymentDate: LocalDateTime? = null,
    val paymentStatus: String? = null,
    val customerName: String? = null,
    val customerEmail: String? = null
) {
    companion object {
        fun fromPayment(payment: Payment): PaymentResponse {
            return PaymentResponse(
                success = true,
                message = "결제 완료",
                orderId = payment.orderId,
                amount = payment.amount,
                paymentDate = payment.createdAt,
                paymentStatus = payment.status.name,
                customerName = payment.customerName,
                customerEmail = payment.customerEmail
            )
        }
    }
}