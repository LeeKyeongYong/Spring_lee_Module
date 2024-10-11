package com.krstudy.kapi.com.krstudy.kapi.standard.ws

import com.krstudy.kapi.com.krstudy.kapi.domain.messages.dto.MessageNotification
import org.springframework.stereotype.Component
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.concurrent.ConcurrentHashMap
import org.springframework.web.socket.CloseStatus

@Component
class MessageWebSocketHandler : TextWebSocketHandler() {
    private val sessions = ConcurrentHashMap<WebSocketSession, String>()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val userId = session.attributes["userId"] as? String ?: "anonymous"
        sessions[session] = userId
        println("WebSocket connection established for user: $userId")
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val userId = sessions[session]
        println("Received message from user $userId: ${message.payload}")
        // 메시지 처리 로직
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val userId = sessions.remove(session)
        println("WebSocket connection closed for user: $userId")
    }

    fun sendMessageToUser(userId: String, notification: MessageNotification) {
        sessions.entries.find { it.value == userId }?.key?.let { session ->
            session.sendMessage(TextMessage(ObjectMapper().writeValueAsString(notification)))
        }
    }
}