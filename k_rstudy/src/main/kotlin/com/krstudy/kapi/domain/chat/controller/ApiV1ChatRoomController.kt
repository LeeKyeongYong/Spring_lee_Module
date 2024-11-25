package com.krstudy.kapi.domain.chat.controller

import com.krstudy.kapi.domain.chat.service.ChatService
import com.krstudy.kapi.domain.chat.dto.ChatCreateReqBody
import com.krstudy.kapi.domain.chat.dto.ChatMessageWriteReqBody
import com.krstudy.kapi.domain.chat.entity.ChatMessage
import com.krstudy.kapi.com.krstudy.kapi.domain.chat.dto.ChatRoomDTO
import com.krstudy.kapi.global.https.ReqData
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/chat/rooms")
class ApiV1ChatRoomController(
    private val chatService: ChatService,
    private val rq: ReqData
) {
    @GetMapping
    fun getChatRooms(): ResponseEntity<List<ChatRoomDTO>> {
        return try {
            val rooms = chatService.getChatRooms()
            val roomDTOs = rooms.map { room ->
                ChatRoomDTO(
                    id = room.id, // ID 포함
                    roomName = room.roomName,
                    authorId = room.author?.userid,
                    authorName = room.author?.nickname, // 필요한 정보만 포함
                    createDate = room.getCreateDate()
                )
            }
            ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(roomDTOs)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(emptyList())
        }
    }

    @GetMapping("/{id}")
    fun getChatRoom(@PathVariable id: Long): ChatRoomDTO {
        val room = chatService.getChatRoom(id)
        return ChatRoomDTO(
            id = room.id, // ID 포함
            roomName = room.roomName,
            authorId = room.author?.userid,
            authorName = room.author?.nickname, // 필요한 정보만 포함
            createDate = room.getCreateDate()
        )
    }

    @PostMapping
    fun createChatRoom(@RequestBody reqBody: ChatCreateReqBody): ResponseEntity<ChatRoomDTO> {
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
            val roomDTO = ChatRoomDTO(
                id = chatRoom.id, // ID 포함
                roomName = chatRoom.roomName,
                authorId = chatRoom.author?.userid,
                authorName = chatRoom.author?.nickname, // 필요한 정보만 포함
                createDate = chatRoom.getCreateDate()
            )
            ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(roomDTO)
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