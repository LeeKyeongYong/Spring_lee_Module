package com.krstudy.kapi.domain.chat.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/chat/rooms")
class ApiV1ChatRoomController {
    @GetMapping
    fun getChatRooms(): String {
        return "채팅방 목록!"
    }
}