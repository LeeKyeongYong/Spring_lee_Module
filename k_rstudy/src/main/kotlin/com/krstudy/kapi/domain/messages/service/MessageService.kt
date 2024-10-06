package com.krstudy.kapi.domain.messages.service

import com.github.benmanes.caffeine.cache.Caffeine
import com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.domain.member.service.MemberService
import com.krstudy.kapi.domain.messages.entity.Message
import com.krstudy.kapi.domain.messages.entity.MessageRecipient
import com.krstudy.kapi.domain.messages.repository.MessageRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

@Service
class MessageService(
    private val messageRepository: MessageRepository,
    private val memberService: MemberService
) {
    // In-memory cache using Caffeine
    private val messageCache = Caffeine.newBuilder()
        .maximumSize(10000)
        .expireAfterWrite(10, TimeUnit.MINUTES)
        .build<Long, Message>()

    private val userCache = Caffeine.newBuilder()
        .maximumSize(300000) // Cache for all 300k users
        .expireAfterWrite(30, TimeUnit.MINUTES)
        .build<Long, Member>()

    @Async
    fun sendMessage(senderId: Long, content: String, recipientIds: List<Long>): CompletableFuture<Message> {
        return CompletableFuture.supplyAsync {
            val message = Message(
                senderId = senderId,
                content = content
            )

            recipientIds.forEach { recipientId ->
                val recipient = userCache.get(recipientId) { memberService.getMemberByNo(it) }
                recipient?.let {
                    message.recipients.add(
                        MessageRecipient(
                            message = message,
                            recipientId = recipientId,
                            recipientName = it.username ?: "",
                            departmentCode = "", // Assuming Member doesn't have departmentCode
                            teamCode = "" // Assuming Member doesn't have teamCode
                        )
                    )
                }
            }

            val savedMessage = messageRepository.save(message)
            messageCache.put(savedMessage.id, savedMessage)

            savedMessage
        }
    }

    fun markAsRead(messageId: Long, recipientId: Long) {
        val message = messageCache.get(messageId) { messageRepository.findById(it).orElseThrow() }
        message.recipients
            .find { it.recipientId == recipientId }
            ?.apply {
                messageRepository.save(message)
            }
    }



}