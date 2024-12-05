package com.krstudy.kapi.domain.payments.repository

import com.krstudy.kapi.domain.payments.entity.Order
import com.krstudy.kapi.domain.payments.status.OrderStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : JpaRepository<Order, Long> {
    fun findByOrderId(orderId: String): Order?

    // 추가적으로 유용할 수 있는 메서드들
    fun findByOrderIdAndStatus(orderId: String, status: OrderStatus): Order?
    fun existsByOrderId(orderId: String): Boolean
    fun findAllByStatus(status: OrderStatus): List<Order>
}