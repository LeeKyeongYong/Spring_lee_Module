package com.krstudy.kapi.domain.messages.entity

import com.krstudy.kapi.domain.messages.dto.RecipientDto
import com.krstudy.kapi.domain.messages.entity.Message
import com.krstudy.kapi.global.jpa.BaseEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class MessageRecipient(
    @ManyToOne
    @JoinColumn(name = "message_id")
    val message: Message,

    val recipientId: Long,
    var recipientName: String,
    var readAt: LocalDateTime? = null
) : BaseEntity()

// MessageResponse 수정
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
                    RecipientDto(it.recipientId, it.recipientName, it.readAt)
                },
                sentAt = message.sentAt,
                readAt = message.getModifyDate() // nullable
            )
        }
    }
}