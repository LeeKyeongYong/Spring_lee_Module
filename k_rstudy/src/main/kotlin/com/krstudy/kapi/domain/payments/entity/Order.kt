package com.krstudy.kapi.domain.payments.entity

import com.krstudy.kapi.domain.payments.status.OrderStatus
import com.krstudy.kapi.global.jpa.BaseEntity
import com.krstudy.kapi.global.exception.GlobalException // GlobalException 클래스 import
import jakarta.persistence.* // JPA 관련 모든 애노테이션 import
import java.math.BigDecimal // BigDecimal 클래스 import

@Entity
@Table(name = "orders")
class Order(


    @Column(nullable = false)
    val orderId: String,

    @Column(nullable = false)
    val amount: BigDecimal,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: OrderStatus = OrderStatus.CREATED
) : BaseEntity() {
    fun markAsPaymentPending() {
        if (status != OrderStatus.CREATED) {
            throw GlobalException("400-10", "잘못된 주문 상태입니다.")
        }
        status = OrderStatus.PAYMENT_PENDING
    }

    fun markAsPaymentCompleted() {
        if (status != OrderStatus.PAYMENT_PENDING) {
            throw GlobalException("400-11", "잘못된 주문 상태입니다.")
        }
        status = OrderStatus.PAYMENT_COMPLETED
    }
}