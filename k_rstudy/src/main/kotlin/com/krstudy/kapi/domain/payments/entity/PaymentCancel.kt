package com.krstudy.kapi.domain.payments.entity

import com.krstudy.kapi.global.jpa.BaseEntity
import java.time.LocalDateTime
import jakarta.persistence.*

@Entity
class PaymentCancel(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    val payment: Payment,

    val cancelReason: String,
    val cancelAmount: Int,
    val transactionKey: String?,
    val canceledAt: LocalDateTime = LocalDateTime.now()
) : BaseEntity()