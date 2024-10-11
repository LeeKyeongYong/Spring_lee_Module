package com.krstudy.kapi.domain.messages.controller

import com.krstudy.kapi.com.krstudy.kapi.domain.messages.dto.MessageNotification
import com.krstudy.kapi.domain.messages.entity.Message
import com.krstudy.kapi.domain.messages.service.MessageService
import org.slf4j.LoggerFactory
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.ui.Model
import java.security.Principal

@Controller
class MessageController(
    private val messageService: MessageService
) {
    private val logger = LoggerFactory.getLogger(MessageController::class.java)

    @GetMapping("/messages")
    suspend fun showMessageList(): String {
        return "domain/messages/messagesList"
    }

 @GetMapping("/messages/writerForm")
    suspend fun showNewMessageForm(): String {//메세지 작성하기
        return "domain/messages/writerMessage"
    }

    //메세지  상세보기페이지
    @GetMapping("/messages/{id}")
    suspend fun showMessageDetail(@PathVariable id: Long, model: Model): String {
        val message = messageService.getMessageById(id)
        val sender = messageService.getMemberById(message.senderId)

        // 메시지를 읽은 것으로 표시 (현재 사용자가 수신자인 경우)
        val currentUser = messageService.getCurrentUser()
        message.recipients.find { it.recipientId == currentUser.id }?.let { recipient ->
            if (recipient.readAt == null) {
                messageService.markAsRead(message.id, currentUser.id)
            }
        }

        model.addAttribute("message", message)
        model.addAttribute("sender", sender)

        return "domain/messages/messageDetail"
    }

    @MessageMapping("/send")
    @SendToUser("/queue/messages")
    suspend fun sendMessage(@Payload message: Message, principal: Principal?): MessageNotification {
        try {
            logger.info("Received message from user: ${principal?.name ?: "Unknown"}")
            logger.info("Message content: ${message.content}, title: ${message.title}")

            val savedMessage = messageService.sendMessage(message)

            logger.info("Saved message ID: ${savedMessage.id}")

            return MessageNotification(
                messageId = savedMessage.id,
                content = savedMessage.content,
                title = savedMessage.title,
                senderId = savedMessage.senderId
            )
        } catch (e: Exception) {
            logger.error("Error processing message: ${e.message}", e)
            throw e
        }
    }
}
