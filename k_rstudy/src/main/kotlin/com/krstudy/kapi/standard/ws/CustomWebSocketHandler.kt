package com.krstudy.kapi.standard.ws
import com.fasterxml.jackson.databind.ObjectMapper
import com.krstudy.kapi.com.krstudy.kapi.domain.messages.dto.MessageNotification
import org.springframework.stereotype.Component
import org.springframework.web.socket.handler.TextWebSocketHandler
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap

@Component
class CustomWebSocketHandler : TextWebSocketHandler() {
    private val sessions = ConcurrentHashMap<String, WebSocketSession>()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val userId = session.uri?.query // Implement proper user identification
        userId?.let { sessions[it] = session }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val userId = session.uri?.query
        userId?.let { sessions.remove(it) }
    }

    fun sendMessageToUser(userId: String, message: MessageNotification) {
        sessions[userId]?.let { session ->
            val payload = ObjectMapper().writeValueAsString(message)
            session.sendMessage(TextMessage(payload))
        }
    }
}