package com.krstudy.kapi.domain.messages.dto

import com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.domain.messages.entity.Message

data class MessageRequest(
    val content: String,
    val recipientId: Long
) {
    fun toMessage(currentUser: Member): Message {
        return Message(
            content = content,
            senderId = currentUser.id,
            recipientIds = listOf(recipientId)
        )
    }
}