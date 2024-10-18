package com.krstudy.kapi.standard.ws

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {

    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        config.enableSimpleBroker("/topic") // 구독 prefix
        config.setApplicationDestinationPrefixes("/app") // 메시지 발행 prefix
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/ws")
            //.setAllowedOriginPatterns("http://localhost:[*]") // 구체적인 origin 패턴 설정
            .withSockJS()
//            .setStreamBytesLimit(512 * 1024)    // 스트리밍 바이트 제한 설정
//            .setHttpMessageCacheSize(1000)       // HTTP 메시지 캐시 크기 설정
//            .setDisconnectDelay(30 * 1000)      // 연결 해제 지연 설정
    }

//    // CORS 설정 추가
//    override fun configureWebSocketTransport(registry: WebSocketTransportRegistration) {
//        registry.setMessageSizeLimit(512 * 1024) // 메시지 크기 제한
//        registry.setSendBufferSizeLimit(512 * 1024)
//        registry.setSendTimeLimit(20 * 1000)
//    }

}