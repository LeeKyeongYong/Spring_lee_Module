package com.krstudy.kapi.domain.payments.status

enum class OrderStatus {
    CREATED,           // 주문 생성
    PAYMENT_PENDING,   // 결제 진행중
    PAYMENT_COMPLETED, // 결제 완료
    PAYMENT_FAILED    // 결제 실패
}