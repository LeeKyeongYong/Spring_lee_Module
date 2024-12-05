package com.krstudy.kapi.domain.payments.status

enum class PaymentStatus {
    PENDING,    // 결제 대기
    COMPLETED,  // 결제 완료
    FAILED      // 결제 실패
}