package com.krstudy.kapi.com.krstudy.kapi.standard.ws

import com.krstudy.kapi.com.krstudy.kapi.domain.messages.dto.MessageNotification
import org.springframework.stereotype.Component
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.concurrent.ConcurrentHashMap

@Component
class MessageWebSocketHandler : TextWebSocketHandler() {
    private val sessions = ConcurrentHashMap<String, WebSocketSession>()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val userId = session.attributes["userId"] as String
        sessions[userId] = session
    }

    fun sendMessageToUser(userId: String, notification: MessageNotification) {
        sessions[userId]?.let { session ->
            session.sendMessage(TextMessage(ObjectMapper().writeValueAsString(notification)))
        }
    }
}