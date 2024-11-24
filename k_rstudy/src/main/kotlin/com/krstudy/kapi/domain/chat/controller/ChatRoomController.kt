package com.krstudy.kapi.domain.chat.controller

import com.krstudy.kapi.global.https.ReqData
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/chat/rooms")
class ChatRoomController(
    private val rq:ReqData
) {
    @GetMapping
    fun getChatRooms(): String {
        rq.setAttribute("",1)
        return ""
    }
}