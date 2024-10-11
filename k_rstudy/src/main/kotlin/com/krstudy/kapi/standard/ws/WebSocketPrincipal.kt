package com.krstudy.kapi.standard.ws

import java.security.Principal

data class WebSocketPrincipal(private val name: String) : Principal {
    override fun getName(): String = name
}