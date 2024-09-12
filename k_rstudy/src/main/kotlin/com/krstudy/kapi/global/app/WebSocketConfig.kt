package com.krstudy.kapi.global.app

import com.krstudy.kapi.standard.ws.ChatWebSocketHandler
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.*
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
@Configuration
@EnableWebSocketMessageBroker
@EnableWebSocket
class WebSocketConfig (
    //private val channelInterceptor: AuthChannelInterceptor
) : WebSocketConfigurer, WebSocketMessageBrokerConfigurer {

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/ws").withSockJS()
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.setApplicationDestinationPrefixes("/app")
        registry.enableSimpleBroker("/topic")
    }

    override fun configureClientInboundChannel(registration: ChannelRegistration) {
       // registration.interceptors(channelInterceptor)
    }

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(ChatWebSocketHandler(), "/chat")
            .setAllowedOrigins("*")
    }
}