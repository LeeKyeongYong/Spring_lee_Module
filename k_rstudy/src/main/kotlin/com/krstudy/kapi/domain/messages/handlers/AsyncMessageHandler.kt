package com.krstudy.kapi.domain.messages.handlers

import kotlinx.coroutines.*
import org.springframework.stereotype.Component
import com.krstudy.kapi.domain.messages.repository.MessageRepository
import com.krstudy.kapi.domain.messages.entity.Message
import org.springframework.web.reactive.socket.WebSocketHandler

@Component
class AsyncMessageHandler(
    private val messageRepository: MessageRepository,
    private val webSocketHandler: WebSocketHandler
) {
    suspend fun processAndSend(message: Message): Message = coroutineScope {
        val savedMessage = async(Dispatchers.IO) {
            messageRepository.save(message)
        }

        launch {
            webSocketHandler.notifyUser(
                message.receiver.userid, // Member 클래스의 userid 필드 사용
                NewMessageNotification(savedMessage.await())
            )
        }

        savedMessage.await()
    }
}