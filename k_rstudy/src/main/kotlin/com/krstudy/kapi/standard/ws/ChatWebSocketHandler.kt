package com.krstudy.kapi.standard.ws

import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.CloseStatus
import java.util.concurrent.ConcurrentHashMap

class ChatWebSocketHandler : TextWebSocketHandler() {

    private val sessions: MutableMap<String, WebSocketSession> = ConcurrentHashMap()

    @Throws(Exception::class)
    override fun afterConnectionEstablished(session: WebSocketSession) {
        val username = getUsername(session)
        sessions[username] = session
    }

    @Throws(Exception::class)
    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val username = getUsername(session)
        val payload = message.payload

        // Broadcast message to all connected users
        sessions.values.forEach { s ->
            if (s.isOpen && s.id != session.id) {
                s.sendMessage(TextMessage("$username: $payload"))
            }
        }
    }

    @Throws(Exception::class)
    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val username = getUsername(session)
        sessions.remove(username)
    }

    private fun getUsername(session: WebSocketSession): String {
        // Extract username from session or use session attributes
        return session.attributes["username"] as String
    }
}
