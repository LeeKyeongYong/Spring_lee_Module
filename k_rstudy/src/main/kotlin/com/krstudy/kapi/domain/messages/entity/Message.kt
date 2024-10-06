package com.krstudy.kapi.domain.messages.entity

import com.krstudy.kapi.global.jpa.BaseEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Message(
    @Column(nullable = false)
    val content: String,

    val senderId: Long,

    val sentAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "message", cascade = [CascadeType.ALL], orphanRemoval = true)
    val recipients: MutableList<MessageRecipient> = mutableListOf()
) : BaseEntity() {
    // BaseEntity에서 상속받은 createDate를 사용
    override fun toString(): String {
        return "Message(id=$id, content='$content', senderId=$senderId, sentAt=$sentAt, recipients=$recipients)"
    }
}