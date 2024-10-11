package com.krstudy.kapi.global.interceptor

import com.krstudy.kapi.global.Security.JwtTokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.stereotype.Component

@Component
class WebSocketAuthInterceptor : HandshakeInterceptor {
    @Autowired
    lateinit var jwtTokenProvider: JwtTokenProvider

    override fun beforeHandshake(request: ServerHttpRequest, response: ServerHttpResponse, wsHandler: WebSocketHandler, attributes: MutableMap<String, Any>): Boolean {
        val token = extractToken(request)
        if (token != null && jwtTokenProvider.validateToken(token)) {
            val userId = jwtTokenProvider.getUserIdFromToken(token)
            attributes["userId"] = userId
            return true
        }
        return false
    }

    override fun afterHandshake(request: ServerHttpRequest, response: ServerHttpResponse, wsHandler: WebSocketHandler, exception: Exception?) {
        // 핸드셰이크 후 처리 (필요한 경우)
    }

    private fun extractToken(request: ServerHttpRequest): String? {
        val headers = request.headers["Authorization"]
        if (headers != null && headers.isNotEmpty()) {
            val header = headers[0]
            if (header.startsWith("Bearer ")) {
                return header.substring(7)
            }
        }
        return null
    }
}