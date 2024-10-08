package com.krstudy.kapi.domain.messages.service

import com.github.benmanes.caffeine.cache.Caffeine
import com.krstudy.kapi.domain.member.dto.MemberDto
import com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.domain.member.service.MemberService
import com.krstudy.kapi.domain.messages.entity.Message
import com.krstudy.kapi.domain.messages.repository.MessageRepository
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

@Service
class MessageService(
    private val messageRepository: MessageRepository,
    private val memberService: MemberService
) {
    private val messageCache = Caffeine.newBuilder()
        .maximumSize(10000)
        .expireAfterWrite(10, TimeUnit.MINUTES)
        .build<Long, Message>()

    private val userCache = Caffeine.newBuilder()
        .maximumSize(300000)
        .expireAfterWrite(30, TimeUnit.MINUTES)
        .build<Long, Member>()

    suspend fun sendMessage(message: Message): Message {
        message.recipients.forEach { recipient ->
            val user = userCache.get(recipient.recipientId) {
                memberService.getMemberByNo(recipient.recipientId)
            }
            user?.let {
                recipient.recipientName = it.username ?: ""
            }
        }

        val savedMessage = messageRepository.save(message)
        messageCache.put(savedMessage.id, savedMessage)
        return savedMessage
    }

    suspend fun getUnreadMessagesCount(memberId: Long): Long {
        return messageRepository.countUnreadMessagesByRecipientId(memberId)
    }

    suspend fun searchUsers(searchUsername: String): List<MemberDto> {
        val members = memberService.searchMembersByUsername(searchUsername)
        return if (members.isNotEmpty()) {
            members.map { MemberDto.from(it) }
        } else {
            emptyList() // 검색 결과 없으면 빈 리스트 반환
        }
    }

    suspend fun getAllUsers(): List<MemberDto> {
        return memberService.getAllMembers().map { MemberDto.from(it) }
    }

    suspend fun getCurrentUser(): Member {
        return memberService.getCurrentUser()
    }

    suspend fun getMessagesForUser(userId: Long, pageable: Pageable): Page<Message> {
        return messageRepository.findMessagesByUserId(userId, pageable)
    }

    suspend fun searchMessages(userId: Long, searchTerm: String, pageable: Pageable): Page<Message> {
        return messageRepository.searchMessages(userId, searchTerm, pageable)
    }

}
