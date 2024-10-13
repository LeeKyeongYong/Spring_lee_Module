package com.krstudy.kapi.standard.ws
import com.fasterxml.jackson.databind.ObjectMapper
import com.krstudy.kapi.com.krstudy.kapi.domain.messages.dto.MessageNotification
import com.krstudy.kapi.com.krstudy.kapi.global.Security.datas.JwtTokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.socket.handler.TextWebSocketHandler
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap

@Component
class CustomWebSocketHandler : TextWebSocketHandler() {
    private val sessions = ConcurrentHashMap<WebSocketSession, String>()

    @Autowired
    private lateinit var jwtTokenProvider: JwtTokenProvider

    override fun afterConnectionEstablished(session: WebSocketSession) {
//        val userId = session.attributes["userId"] as? String ?: "anonymous"
//        sessions[session] = userId
//        println("WebSocket connection established for user: $userId")
        val token = session.handshakeHeaders["Authorization"]?.firstOrNull()?.removePrefix("Bearer ")
        if (token != null && jwtTokenProvider.validateToken(token)) {
            val userId = jwtTokenProvider.getUserIdFromToken(token)
            session.attributes["userId"] = userId
            super.afterConnectionEstablished(session)
        } else {
            session.close(CloseStatus.POLICY_VIOLATION)
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val userId = sessions.remove(session)
        println("WebSocket connection closed for user: $userId")
    }

    fun sendMessageToUser(userId: String, message: MessageNotification) {
        sessions.entries.find { it.value == userId }?.key?.let { session ->
            val payload = ObjectMapper().writeValueAsString(message)
            session.sendMessage(TextMessage(payload))
        }
    }
}