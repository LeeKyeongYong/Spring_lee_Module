package com.krstudy.kapi.com.krstudy.kapi.domain.messages.dto

data class MessageNotification(
    val messageId: Long,
    val content: String,
    val senderId: Long
)
