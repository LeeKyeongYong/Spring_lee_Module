package com.krstudy.kapi.domain.messages.entity

import com.krstudy.kapi.domain.messages.entity.Message
import com.krstudy.kapi.global.jpa.BaseEntity
import jakarta.persistence.*

@Entity
data class MessageRecipient(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id")
    val message: Message,

    @Column(nullable = false)
    val recipientId: Long,

    @Column(nullable = false)
    val recipientName: String,

    @Column(nullable = false)
    val departmentCode: String,

    @Column(nullable = false)
    val teamCode: String
): BaseEntity() {}