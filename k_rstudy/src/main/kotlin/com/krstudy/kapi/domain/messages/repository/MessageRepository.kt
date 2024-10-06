package com.krstudy.kapi.domain.messages.repository

import com.krstudy.kapi.domain.messages.entity.Message
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository : JpaRepository<Message, Long> {
    fun findByRecipientsRecipientIdOrderByCreatedAtDesc(recipientId: Long, pageable: Pageable): Page<Message>

    @Query("SELECT COUNT(m) FROM Message m JOIN m.recipients r WHERE r.recipientId = :recipientId AND r.readAt IS NULL")
    fun countUnreadMessagesByRecipientId(recipientId: Long): Long

    @Query("SELECT m FROM Message m WHERE m.senderId = :userId OR :userId IN (SELECT r.recipientId FROM m.recipients r)")
    fun findMessagesByUserId(userId: Long, pageable: Pageable): Page<Message>
}