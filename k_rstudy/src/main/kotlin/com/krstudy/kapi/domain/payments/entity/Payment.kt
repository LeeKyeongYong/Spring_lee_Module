package com.krstudy.kapi.domain.payments.entity

import com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.domain.payments.dto.PaymentResponseDto
import com.krstudy.kapi.domain.payments.status.PaymentStatus
import com.krstudy.kapi.global.jpa.BaseEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "payments")
class Payment(

    @Column(nullable = false)
    var orderId: String,

    @Column(nullable = false)
    var amount: Int,

    @Column(nullable = false)
    var paymentKey: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: PaymentStatus = PaymentStatus.PENDING,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    var member: Member? = null,

    @Column(nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column
    var completedAt: LocalDateTime? = null
) : BaseEntity()

fun Payment.toDto() = PaymentResponseDto(
    orderId = this.orderId,
    amount = this.amount,
    status = this.status,
    paymentKey = this.paymentKey,
    completedAt = this.completedAt
)