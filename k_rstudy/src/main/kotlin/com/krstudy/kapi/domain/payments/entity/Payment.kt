package com.krstudy.kapi.domain.payments.entity

import com.krstudy.kapi.domain.payments.status.PaymentStatus
import com.krstudy.kapi.global.jpa.BaseEntity
import java.math.BigDecimal
import java.time.LocalDateTime
import jakarta.persistence.*

@Entity
@Table(name = "payments")
class Payment private constructor(

    @Column(nullable = false)
    val orderId: String,

    @Column(nullable = false)
    val amount: BigDecimal,

    @Column(nullable = false)
    val paymentKey: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: PaymentStatus,

    @Column(nullable = false)
    val customerKey: String,

    @Column(nullable = false)
    val customerName: String,

    @Column(nullable = false)
    val customerEmail: String,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
): BaseEntity() {
    companion object {
        fun create(
            orderId: String,
            amount: BigDecimal,
            paymentKey: String,
            customerKey: String,
            customerName: String,
            customerEmail: String
        ): Payment {
            return Payment(
                orderId = orderId,
                amount = amount,
                paymentKey = paymentKey,
                status = PaymentStatus.PENDING,
                customerKey = customerKey,
                customerName = customerName,
                customerEmail = customerEmail
            )
        }
    }

    fun complete() {
        this.status = PaymentStatus.COMPLETED
    }

    fun fail() {
        this.status = PaymentStatus.FAILED
    }
}