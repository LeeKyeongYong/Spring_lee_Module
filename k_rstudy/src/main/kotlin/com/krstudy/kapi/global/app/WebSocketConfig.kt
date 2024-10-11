package com.krstudy.kapi.global.app

import com.krstudy.kapi.com.krstudy.kapi.standard.ws.MessageWebSocketHandler
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
    private lateinit var customWebSocketHandler: CustomWebSocketHandler

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(customWebSocketHandler, "/ws")
            .setAllowedOrigins("*")
            .withSockJS()
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/ws")
            .setAllowedOrigins("http://localhost:8090")
            .withSockJS()
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {

        registry.enableSimpleBroker("/queue", "/topic")
        registry.setApplicationDestinationPrefixes("/app")
        //registry.setUserDestinationPrefix("/user")
    }

    @Bean
    fun messageWebSocketHandler() = MessageWebSocketHandler()
}