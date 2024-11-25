package com.krstudy.kapi.domain.chat.controller

import com.krstudy.kapi.domain.chat.service.*
import com.krstudy.kapi.domain.chat.dto.ChatCreateReqBody
import com.krstudy.kapi.domain.chat.entity.ChatMessage
import com.krstudy.kapi.domain.chat.entity.ChatRoom
import org.springframework.web.bind.annotation.*
import com.krstudy.kapi.domain.chat.dto.ChatMessageWriteReqBody
import com.krstudy.kapi.global.https.ReqData
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity


@RestController
@RequestMapping("/api/v1/chat/rooms")
class ApiV1ChatRoomController(
    private val chatService: ChatService,
    private val rq: ReqData
) {
    @GetMapping
    fun getChatRooms(): ResponseEntity<List<ChatRoom>> {
        return try {
            val rooms = chatService.getChatRooms()
            ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(rooms)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(emptyList())
        }
    }
    @GetMapping("/{id}")
    fun getChatRoom(@PathVariable id: Long): ChatRoom = chatService.getChatRoom(id)

    @PostMapping
    fun createChatRoom(@RequestBody reqBody: ChatCreateReqBody): ResponseEntity<ChatRoom> {
        val member = rq.getMember()

        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(null)
        }

        return try {
            val chatRoom = chatService.createChatRoom(
                roomName = reqBody.roomName,
                author = member
            )
            ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(chatRoom)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(null)
        }
    }


    @GetMapping("/{chatRoomId}/messages")
    fun getChatMessages(
        @PathVariable chatRoomId: Long,
        @RequestParam(defaultValue = "-1") afterChatMessageId: Long
    ): List<ChatMessage> {
        return chatService.getChatMessagesAfter(chatRoomId, afterChatMessageId)
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