package com.krstudy.kapi.domain.chat.service

import com.krstudy.kapi.domain.chat.entity.ChatMessage
import com.krstudy.kapi.domain.chat.entity.ChatRoom
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ChatService {
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

    private val chatMessagesByRoomId = mutableMapOf<Long, MutableList<ChatMessage>>(
        1L to mutableListOf(
            ChatMessage(
                chatRoomId = 1L,
                writerName = "김철수",
                content = "풋살하실 분 계신가요?"
            ).apply {
                id = 1L
                setCreateDate(LocalDateTime.now())
                setModifyDate(LocalDateTime.now())
            },
            ChatMessage(
                chatRoomId = 1L,
                writerName = "이영희",
                content = "네, 저요!"
            ).apply {
                id = 2L
                setCreateDate(LocalDateTime.now())
                setModifyDate(LocalDateTime.now())
            }
        ),
        2L to mutableListOf(
            ChatMessage(
                chatRoomId = 2L,
                writerName = "박철수",
                content = "농구하실 분 계신가요?"
            ).apply {
                id = 3L
                setCreateDate(LocalDateTime.now())
                setModifyDate(LocalDateTime.now())
            },
            ChatMessage(
                chatRoomId = 2L,
                writerName = "김영희",
                content = "네, 저요!"
            ).apply {
                id = 4L
                setCreateDate(LocalDateTime.now())
                setModifyDate(LocalDateTime.now())
            }
        ),
        3L to mutableListOf(
            ChatMessage(
                chatRoomId = 3L,
                writerName = "이철수",
                content = "야구하실 분 계신가요?"
            ).apply {
                id = 5L
                setCreateDate(LocalDateTime.now())
                setModifyDate(LocalDateTime.now())
            },
            ChatMessage(
                chatRoomId = 3L,
                writerName = "박영희",
                content = "네, 저요!"
            ).apply {
                id = 6L
                setCreateDate(LocalDateTime.now())
                setModifyDate(LocalDateTime.now())
            }
        )
    )

    fun getChatRooms(): List<ChatRoom> = chatRooms

    fun getChatRoom(id: Long): ChatRoom {
        return chatRooms.find { it.id == id }
            ?: throw NoSuchElementException("Chat room not found with id: $id")
    }

    fun createChatRoom(roomName: String): ChatRoom {
        val chatRoom = ChatRoom(roomName = roomName).apply {
            id = chatRooms.size + 1L
            setCreateDate(LocalDateTime.now())
            setModifyDate(LocalDateTime.now())
        }
        chatRooms.add(chatRoom)
        return chatRoom
    }

    fun getChatMessages(chatRoomId: Long): List<ChatMessage> {
        return chatMessagesByRoomId.getOrDefault(chatRoomId, emptyList())
    }

    fun writeChatMessage(
        chatRoomId: Long,
        writerName: String,
        content: String
    ): ChatMessage {
        val chatMessages = chatMessagesByRoomId.getOrPut(chatRoomId) { mutableListOf() }
        val chatMessage = ChatMessage(
            chatRoomId = chatRoomId,
            writerName = writerName,
            content = content
        ).apply {
            id = chatMessages.size + 1L
            setCreateDate(LocalDateTime.now())
            setModifyDate(LocalDateTime.now())
        }
        chatMessages.add(chatMessage)
        return chatMessage
    }
}