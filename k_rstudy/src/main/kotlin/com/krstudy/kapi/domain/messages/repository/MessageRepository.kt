package com.krstudy.kapi.domain.messages.repository

import com.krstudy.kapi.domain.messages.entity.Message
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository : JpaRepository<Message, Long> {
    fun findByRecipientsRecipientIdOrderByCreatedAtDesc(recipientId: Long, pageable: Pageable): Page<Message>
}