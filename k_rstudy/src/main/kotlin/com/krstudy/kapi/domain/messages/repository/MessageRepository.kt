package com.krstudy.kapi.domain.messages.repository

import com.krstudy.kapi.domain.messages.entity.Message
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository : JpaRepository<Message, Long> {
    // createDate는 BaseEntity에서 상속받은 필드이므로 이를 사용
    fun findByRecipientsRecipientIdOrderByCreateDateDesc(recipientId: Long, pageable: Pageable): Page<Message>

    // MessageRecipient의 readAt 필드를 참조하도록 수정
    @Query("""
        SELECT COUNT(DISTINCT m) 
        FROM Message m 
        JOIN MessageRecipient mr ON mr.message = m 
        WHERE mr.recipientId = :recipientId 
        AND mr.readAt IS NULL
    """)
    fun countUnreadMessagesByRecipientId(recipientId: Long): Long

    @Query("""
        SELECT DISTINCT m 
        FROM Message m 
        LEFT JOIN MessageRecipient mr ON mr.message = m 
        WHERE m.senderId = :userId 
        OR mr.recipientId = :userId
    """)
    fun findMessagesByUserId(userId: Long, pageable: Pageable): Page<Message>
}