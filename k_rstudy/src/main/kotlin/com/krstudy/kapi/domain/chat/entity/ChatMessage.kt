package com.krstudy.kapi.domain.chat.entity

import com.krstudy.kapi.global.jpa.BaseEntity

data class ChatMessage(
    var chatRoomId: Long = 0,
    var writerName: String? = null,
    var content: String? = null
):BaseEntity(){}