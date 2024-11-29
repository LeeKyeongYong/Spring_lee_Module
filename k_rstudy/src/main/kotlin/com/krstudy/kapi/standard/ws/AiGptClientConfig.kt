package com.krstudy.kapi.standard.ws

import org.springframework.context.annotation.Configuration
import org.springframework.ai.chat.client.ChatClient
import org.springframework.context.annotation.Bean
@Configuration
class AiGptClientConfig {
    @Bean
    fun chatClient(builder: ChatClient.Builder): ChatClient {
        return builder.build()
    }
}