package com.krstudy.kapi.com.krstudy.kapi.domain.chat.dto

data class ChatRoomDTO(
    val id: Long,
    val roomName: String?,
    val authorName: String? // 필요한 필드만 포함
)