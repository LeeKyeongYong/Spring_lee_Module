package com.krstudy.kapi.domain.payments.dto

data class TossPaymentResult(
    val customerKey: String,
    val customerName: String,
    val customerEmail: String
)