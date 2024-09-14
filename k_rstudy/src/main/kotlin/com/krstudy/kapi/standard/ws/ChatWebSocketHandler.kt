package com.krstudy.kapi.standard.ws

import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.CloseStatus
import java.util.concurrent.ConcurrentHashMap

/**
 * WebSocket을 사용하여 채팅 기능을 처리하는 핸들러 클래스이다.
 * 사용자의 메시지를 브로드캐스트하고, WebSocket 연결을 관리한다.
 */
class ChatWebSocketHandler : TextWebSocketHandler() {

    /**
     * 현재 연결된 WebSocket 세션을 저장하는 맵 이다.
     * 사용자 이름을 키로 하여 WebSocket 세션을 저장 한다.
     */
    private val sessions: MutableMap<String, WebSocketSession> = ConcurrentHashMap()

    /**
     * WebSocket 연결이 설정된 후 호출 된다.
     * 연결된 세션의 사용자 이름을 추출하여 세션을 `sessions` 맵에 저장한다.
     *
     * @param session 연결된 WebSocket 세션
     * @throws Exception WebSocket 연결 설정 중 발생할 수 있는 예외
     */
    @Throws(Exception::class)
    override fun afterConnectionEstablished(session: WebSocketSession) {
        val username = getUsername(session)
        sessions[username] = session
    }

    /**
     * 클라이언트로부터 텍스트 메시지를 수신할 때 호출된다.
     * 수신한 메시지를 모든 연결된 사용자에게 브로드캐스트한다.
     *
     * @param session 메시지를 수신한 WebSocket 세션
     * @param message 수신한 텍스트 메시지
     * @throws Exception 메시지를 처리하는 도중 발생할 수 있는 예외
     */
    @Throws(Exception::class)
    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val username = getUsername(session)
        val payload = message.payload

        // 모든 연결된 사용자에게 메시지를 브로드캐스트한다.
        sessions.values.forEach { s ->
            if (s.isOpen && s.id != session.id) {
                s.sendMessage(TextMessage("$username: $payload"))
            }
        }
    }

    /**
     * WebSocket 연결이 종료된 후 호출된다.
     * 종료된 세션의 사용자 이름을 추출하여 `sessions` 맵에서 해당 세션을 제거한다.
     *
     * @param session 종료된 WebSocket 세션
     * @param status 종료 상태
     * @throws Exception WebSocket 연결 종료 중 발생할 수 있는 예외
     */
    @Throws(Exception::class)
    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val username = getUsername(session)
        sessions.remove(username)
    }

    /**
     * WebSocket 세션에서 사용자 이름을 추출한다.
     * 세션의 속성에서 사용자 이름을 가져온다.
     *
     * @param session 사용자 이름을 추출할 WebSocket 세션
     * @return 세션에서 추출한 사용자 이름
     */
    private fun getUsername(session: WebSocketSession): String {
        // 세션에서 사용자 이름을 추출하거나 세션 속성을 사용한다.
        return session.attributes["username"] as String
    }
}