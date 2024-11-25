package com.krstudy.kapi.domain.chat.controller

import com.krstudy.kapi.domain.chat.service.ChatService
import com.krstudy.kapi.global.https.ReqData
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/chat/rooms")
class ChatRoomController(
    private val rq:ReqData,
    private val chatService: ChatService
) {
    @GetMapping
    fun getChatRooms(): String {
        return "domain/chat/chatList"
    }

    @GetMapping("/{id}")
    fun showChatRoom(@PathVariable id: Long): String {
        val chatRoom = chatService.getChatRoom(id)
        rq.setAttribute("chatRoom", chatRoom)
        return "domain/chat/chatView"
    }

}