package com.krstudy.kapi.domain.chat.controller

import com.krstudy.kapi.domain.chat.entity.ChatRoom
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime


@RestController
@RequestMapping("/api/v1/chat/rooms")
class ApiV1ChatRoomController {

        private val chatRooms = mutableListOf(
            ChatRoom(name = "풋살하실 분?").apply {
                id = 1
                setCreateDate(LocalDateTime.now())
                setModifyDate(LocalDateTime.now())
            },
            ChatRoom(name = "농구 하실 분?").apply {
                id = 2
                setCreateDate(LocalDateTime.now())
                setModifyDate(LocalDateTime.now())
            },
            ChatRoom(name = "야구 하실 분?").apply {
                id = 3
                setCreateDate(LocalDateTime.now())
                setModifyDate(LocalDateTime.now())
            }
        )
        @GetMapping
        fun getChatRooms(): List<ChatRoom> = chatRooms

    }