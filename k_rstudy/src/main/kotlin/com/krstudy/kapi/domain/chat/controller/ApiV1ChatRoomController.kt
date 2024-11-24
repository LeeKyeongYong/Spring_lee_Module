package com.krstudy.kapi.domain.chat.controller

import com.krstudy.kapi.domain.chat.dto.ChatCreateReqBody
import com.krstudy.kapi.domain.chat.entity.ChatRoom
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime


@RestController
@RequestMapping("/api/v1/chat/rooms")
class ApiV1ChatRoomController {

        private val chatRooms = mutableListOf(
            ChatRoom(roomName = "풋살하실 분?").apply {
                id = 1
                setCreateDate(LocalDateTime.now())
                setModifyDate(LocalDateTime.now())
            },
            ChatRoom(roomName = "농구 하실 분?").apply {
                id = 2
                setCreateDate(LocalDateTime.now())
                setModifyDate(LocalDateTime.now())
            },
            ChatRoom(roomName = "야구 하실 분?").apply {
                id = 3
                setCreateDate(LocalDateTime.now())
                setModifyDate(LocalDateTime.now())
            }
        )
        @GetMapping
        fun getChatRooms(): List<ChatRoom> = chatRooms


    @GetMapping("/{id}")
    fun getChatRoom(@PathVariable id: Long): ChatRoom {
            return chatRooms.find { it.id == id }
                ?: throw NoSuchElementException("Chat room not found with id: $id")

    }

    @PostMapping
    fun createChatRoom(@RequestBody reqBody: ChatCreateReqBody): ChatRoom {
        val chatRoom = ChatRoom(roomName = reqBody.roomName).apply {
            id = chatRooms.size + 1L
            setCreateDate(LocalDateTime.now())
            setModifyDate(LocalDateTime.now())
        }

        chatRooms.add(chatRoom)

        return chatRoom
    }
}