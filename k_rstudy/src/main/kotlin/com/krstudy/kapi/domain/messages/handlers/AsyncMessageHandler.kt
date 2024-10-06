package com.krstudy.kapi.domain.messages.handlers

import com.krstudy.kapi.com.krstudy.kapi.domain.messages.dto.MessageNotification
import kotlinx.coroutines.*
import org.springframework.stereotype.Component
import com.krstudy.kapi.domain.messages.repository.MessageRepository
import com.krstudy.kapi.domain.messages.entity.Message
import com.krstudy.kapi.standard.ws.CustomWebSocketHandler

@Component
class AsyncMessageHandler(
    private val messageRepository: MessageRepository,
    private val webSocketHandler: CustomWebSocketHandler
) {
    suspend fun processAndSend(message: Message): Message = coroutineScope {
        val savedMessage = async(Dispatchers.IO) {
            messageRepository.save(message)
        }

        launch {
            val saved = savedMessage.await()
            message.recipients.forEach { recipient ->
                webSocketHandler.sendMessageToUser(
                    userId = recipient.recipientId.toString(),
                    message = MessageNotification(
                        messageId = saved.id,
                        content = message.content,
                        senderId = message.senderId
                    )
                )
            }
        }

        savedMessage.await()
    }
}