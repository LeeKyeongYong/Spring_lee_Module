package com.krstudy.kapi.global.app

import com.krstudy.kapi.com.krstudy.kapi.standard.ws.MessageWebSocketHandler
import com.krstudy.kapi.global.interceptor.WebSocketAuthInterceptor
import com.krstudy.kapi.standard.ws.ChatWebSocketHandler
import com.krstudy.kapi.standard.ws.CustomWebSocketHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.socket.config.annotation.*
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
@EnableWebSocket
class WebSocketConfig : WebSocketConfigurer, WebSocketMessageBrokerConfigurer {

    @Autowired
    private lateinit var webSocketAuthInterceptor: WebSocketAuthInterceptor

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        // WebSocket 핸들러를 등록합니다.
        // 예를 들어:
        // registry.addHandler(customWebSocketHandler, "/ws").setAllowedOrigins("http://localhost:8090")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/ws")
            .setAllowedOrigins("http://localhost:8090")
            .addInterceptors(webSocketAuthInterceptor)
            .withSockJS()
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker("/queue", "/topic")
        registry.setApplicationDestinationPrefixes("/app")
        registry.setUserDestinationPrefix("/user")
    }

    override fun configureWebSocketTransport(registry: WebSocketTransportRegistration) {
        registry.setMessageSizeLimit(64 * 1024) // 64KB
        registry.setSendBufferSizeLimit(512 * 1024) // 512KB
        registry.setSendTimeLimit(20000) // 20 seconds
    }

    @Bean
    fun messageWebSocketHandler() = MessageWebSocketHandler()
}