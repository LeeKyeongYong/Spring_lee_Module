package com.krstudy.kapi.domain.chat.controller

import com.krstudy.kapi.domain.chat.dto.ChatCreateReqBody
import com.krstudy.kapi.domain.chat.entity.ChatMessage
import com.krstudy.kapi.domain.chat.entity.ChatRoom
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import com.krstudy.kapi.standard.base.Ut


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

        // 통신지연을 일부러 발생시키는 코드
        //Ut.thread.sleep(500);

        val chatRoom = ChatRoom(roomName = reqBody.roomName).apply {
            id = chatRooms.size + 1L
            setCreateDate(LocalDateTime.now())
            setModifyDate(LocalDateTime.now())
        }

        chatRooms.add(chatRoom)

        return chatRoom
    }
    //메세지받기


    @GetMapping("/{chatRoomId}/messages")
    fun getChatMessages(@PathVariable chatRoomId: Int): List<ChatMessage> {
        return chatMessagesByRoomId.getOrDefault(chatRoomId, emptyList())
    }

    private val chatMessagesByRoomId = mutableMapOf(
        1 to mutableListOf(
            ChatMessage(

                chatRoomId = 1,
                writerName = "김철수",
                content = "풋살하실 분 계신가요?"
            ).apply {
                id = 1L  // id 설정
                setCreateDate(LocalDateTime.now())
                setModifyDate(LocalDateTime.now())
            },
            ChatMessage(

                chatRoomId = 1,
                writerName = "이영희",
                content = "네, 저요!"
            ).apply {
                id = 2L  // id 설정
                setCreateDate(LocalDateTime.now())
                setModifyDate(LocalDateTime.now())
            }
        ),
        2 to mutableListOf(
            ChatMessage(

                chatRoomId = 2,
                writerName = "박철수",
                content = "농구하실 분 계신가요?"
            ).apply {
                id = 3L  // id 설정
                setCreateDate(LocalDateTime.now())
                setModifyDate(LocalDateTime.now())
            },
            ChatMessage(

                chatRoomId = 2,
                writerName = "김영희",
                content = "네, 저요!"
            ).apply {
                id = 4L  // id 설정
                setCreateDate(LocalDateTime.now())
                setModifyDate(LocalDateTime.now())
            }
        ),
        3 to mutableListOf(
            ChatMessage(

                chatRoomId = 3,
                writerName = "이철수",
                content = "야구하실 분 계신가요?"
            ).apply {
                id = 5L  // id 설정
                setCreateDate(LocalDateTime.now())
                setModifyDate(LocalDateTime.now())
            },
            ChatMessage(

                chatRoomId = 3,
                writerName = "박영희",
                content = "네, 저요!"
            ).apply {
                id = 6L  // id 설정
                setCreateDate(LocalDateTime.now())
                setModifyDate(LocalDateTime.now())
            }
        )
    )

}