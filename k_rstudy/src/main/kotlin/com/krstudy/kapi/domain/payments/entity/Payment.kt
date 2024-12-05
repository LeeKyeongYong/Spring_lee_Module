package com.krstudy.kapi.domain.payments.entity

import com.krstudy.kapi.domain.member.entity.Member
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member,  // 결제한 회원 정보

    @Column(name = "member_userid", nullable = false)
    val memberUserId: String, // 회원 ID (조회 최적화용)

    @Column(name = "member_username", nullable = false)
    val memberUsername: String, // 회원 이름 (조회 최적화용)

    @Column(nullable = false)
    val customerKey: String,

    @Column(nullable = false)
    val customerName: String,

    @Column(nullable = false)
    val customerEmail: String,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
) : BaseEntity() {

    companion object {
        fun create(
            orderId: String,
            amount: BigDecimal,
            paymentKey: String,
            member: Member,
            customerKey: String,
            customerName: String,
            customerEmail: String
        ): Payment {
            return Payment(
                orderId = orderId,
                amount = amount,
                paymentKey = paymentKey,
                status = PaymentStatus.PENDING,
                member = member,
                memberUserId = member.userid,
                memberUsername = member.username ?: "Unknown",
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