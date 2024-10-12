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
        // WebSocket 핸들러를 등록하는 로직을 작성합니다.
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
    @Bean
    fun messageWebSocketHandler() = MessageWebSocketHandler()
}
