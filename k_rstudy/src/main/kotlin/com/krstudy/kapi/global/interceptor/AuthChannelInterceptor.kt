package com.krstudy.kapi.com.krstudy.kapi.global.interceptor

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.AuthenticationException
//import org.springframework.security.core.AuthenticationCredentialsNotFoundException
import org.springframework.stereotype.Service
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException

@Service
class AuthChannelInterceptor @Autowired constructor(
   // private val jwtTokenProvider: JwtTokenProvider
) : ChannelInterceptor {
//
//    @Throws(AuthenticationException::class)
//    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*> {
//        val accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java)
//
//        if (accessor?.command == StompCommand.CONNECT) {
//            val accessToken = accessor.getFirstNativeHeader("Authorization")
//            if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
//                val authentication: Authentication = jwtTokenProvider.getAuthentication(accessToken)
//                SecurityContextHolder.getContext().authentication = authentication
//            } else {
//                throw AuthenticationCredentialsNotFoundException("AccessToken is null or not valid")
//            }
//        }
//        return message
//    }
}