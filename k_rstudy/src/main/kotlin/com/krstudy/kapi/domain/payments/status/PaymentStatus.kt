package com.krstudy.kapi.domain.payments.status


enum class PaymentStatus {
    PENDING,
    COMPLETED,
    FAILED,
    CANCELLED,
    CANCELED,
    READY,
    IN_PROGRESS,
    PARTIAL_CANCELED,
    EXPIRED;

    fun isCancelable(): Boolean {
        return this == COMPLETED || this == PARTIAL_CANCELED
    }
}