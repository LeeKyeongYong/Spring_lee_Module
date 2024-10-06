package com.krstudy.kapi.domain.messages.dto

import com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.domain.messages.entity.Message
import com.krstudy.kapi.domain.messages.entity.MessageRecipient

data class MessageRequest(
    val content: String,
    val recipientId: Long
) {
    fun toMessage(currentUser: Member): Message {
        val message = Message(
            content = content,
            senderId = currentUser.id,
            recipients = mutableListOf()
        )
        message.recipients.add(
            MessageRecipient(
                message = message,
                recipientId = recipientId,
                recipientName = "" // will be filled by service
            )
        )
        return message
    }
}