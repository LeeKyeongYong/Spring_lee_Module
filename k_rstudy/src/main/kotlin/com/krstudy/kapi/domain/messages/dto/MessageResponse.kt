package com.krstudy.kapi.domain.messages.dto

import com.krstudy.kapi.domain.messages.entity.Message
import org.bouncycastle.asn1.cms.RecipientInfo
import java.time.LocalDateTime

data class MessageResponse(
    val id: Long,
    val content: String,
    val title: String,
    val senderId: Long?,
    val senderName: String? = null, // 기본값 설정
    val senderUserId: String? = null, // 기본값 설정
    val recipients: List<RecipientDto>,
    val sentAt: LocalDateTime,
    val readAt: LocalDateTime?
) {
    companion object {
        fun fromMessage(message: Message, includeSenderId: Boolean = false): MessageResponse {
            val sender = message.sender // Message 엔티티에서 sender 관계를 가져옴
            return MessageResponse(
                id = message.id,
                content = message.content,
                title = message.title,
                senderId = message.senderId,
                senderName = sender?.username, // sender의 username
                senderUserId = sender?.userid, // sender의 userid
                recipients = message.recipients.map {
                    RecipientDto(
                        recipientId = it.recipientId,
                        recipientName = it.recipientName,
                        recipientUserId = it.recipientUserId
                    )
                },
                sentAt = message.sentAt,
                readAt = message.getModifyDate()
            )
        }
    }
}
