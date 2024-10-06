package com.krstudy.kapi.domain.messages.entity


import com.krstudy.kapi.global.jpa.BaseEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class Message(
    @Column(nullable = false)
    val senderId: Long,

    @Column(nullable = false)
    val content: String,

    @OneToMany(mappedBy = "message", cascade = [CascadeType.ALL])
    val recipients: MutableList<MessageRecipient> = mutableListOf()
) : BaseEntity() {
    @Column(name = "created_at")
    lateinit var createdAt: LocalDateTime

    @PrePersist
    fun prePersist() {
        createdAt = LocalDateTime.now()
    }
}