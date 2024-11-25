package com.krstudy.kapi.domain.chat.dto

data class ChatMessageWriteReqBody(
    val writerName: String,
    val content: String
)
