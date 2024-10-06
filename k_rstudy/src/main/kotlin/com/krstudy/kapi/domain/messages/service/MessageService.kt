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

    suspend fun searchUsers(searchTerm: String): List<MemberDto> {
        return memberService.searchMembersByUsername(searchTerm)
            .map { MemberDto.from(it) }
    }
}
