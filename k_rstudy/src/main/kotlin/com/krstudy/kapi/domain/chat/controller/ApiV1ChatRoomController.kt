package com.krstudy.kapi.domain.chat.controller

import com.krstudy.kapi.domain.chat.service.*
import com.krstudy.kapi.domain.chat.dto.ChatCreateReqBody
import com.krstudy.kapi.domain.chat.entity.ChatMessage
import com.krstudy.kapi.domain.chat.entity.ChatRoom
import org.springframework.web.bind.annotation.*
import com.krstudy.kapi.domain.chat.dto.ChatMessageWriteReqBody


@RestController
@RequestMapping("/api/v1/chat/rooms")
class ApiV1ChatRoomController ( private val chatService: ChatService) {
    @GetMapping
    fun getChatRooms(): List<ChatRoom> = chatService.getChatRooms()

    @GetMapping("/{id}")
    fun getChatRoom(@PathVariable id: Long): ChatRoom = chatService.getChatRoom(id)

    @PostMapping
    fun createChatRoom(@RequestBody reqBody: ChatCreateReqBody): ChatRoom {
        return chatService.createChatRoom(reqBody.roomName)
    }

    @GetMapping("/{chatRoomId}/messages")
    fun getChatMessages(@PathVariable chatRoomId: Long): List<ChatMessage> {
        return chatService.getChatMessages(chatRoomId)
    }

    @PostMapping("/{chatRoomId}/messages")
    fun writeChatMessage(
        @PathVariable chatRoomId: Long,
        @RequestBody reqBody: ChatMessageWriteReqBody
    ): ChatMessage {
        return chatService.writeChatMessage(
            chatRoomId = chatRoomId,
            writerName = reqBody.writerName,
            content = reqBody.content
        )
    }
}
