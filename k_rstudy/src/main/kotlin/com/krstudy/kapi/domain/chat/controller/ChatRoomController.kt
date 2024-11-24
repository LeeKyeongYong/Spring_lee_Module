package com.krstudy.kapi.domain.chat.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/chat/rooms")
class ChatRoomController {
    @GetMapping
    fun getChatRooms(): String {
        return "채팅방 목록!"
    }
}