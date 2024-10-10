package com.krstudy.kapi.domain.messages.dto

import com.krstudy.kapi.domain.messages.entity.Message
import org.bouncycastle.asn1.cms.RecipientInfo
import java.time.LocalDateTime

data class MessageResponse(
    val id: Long,
    val content: String,
    val title: String,
    val senderId: Long?,  // senderId를 nullable로 유지
    val recipients: List<RecipientDto>,
    val sentAt: LocalDateTime,
    val readAt: LocalDateTime?
) {
    companion object {
        fun fromMessage(message: Message, includeSenderId: Boolean = false): MessageResponse {
            return MessageResponse(
                id = message.id,
                content = message.content,
                title = message.title,
                senderId = if (includeSenderId) message.senderId else null,  // 기본적으로 senderId는 null
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
