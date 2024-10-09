package com.krstudy.kapi.domain.messages.dto

import com.krstudy.kapi.domain.messages.entity.Message
import org.bouncycastle.asn1.cms.RecipientInfo
import java.time.LocalDateTime

data class MessageResponse(
    val id: Long,
    val content: String,
    val senderId: Long,
    val recipients: List<RecipientDto>,
    val sentAt: LocalDateTime,
    val readAt: LocalDateTime?
) {
    companion object {
        fun fromMessage(message: Message): MessageResponse {
            return MessageResponse(
                id = message.id,
                content = message.content,
                senderId = message.senderId,
                recipients = message.recipients.map {
                    RecipientDto(
                        recipientId = it.recipientId,
                        recipientName = it.recipientName,
                        recipientUserId = it.recipientUserId // 추가
                    )
                },
                sentAt = message.sentAt,
                readAt = message.getModifyDate()
            )
        }
    }
}