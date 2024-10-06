package com.krstudy.kapi.domain.messages.dto

import java.time.LocalDateTime

data class RecipientDto(
    val recipientId: Long,
    val recipientName: String,
    val readAt: LocalDateTime?
)