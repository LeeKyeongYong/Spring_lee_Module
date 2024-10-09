package com.krstudy.kapi.domain.messages.controller

import com.krstudy.kapi.domain.messages.service.MessageService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.ui.Model

@Controller
@RequestMapping("/messages")
class MessageController(
    private val messageService: MessageService
) {
    @GetMapping
    suspend fun showMessageList(): String {
        return "domain/messages/messagesList"
    }

 @GetMapping("/writerForm")
    suspend fun showNewMessageForm(): String {//메세지 작성하기
        return "domain/messages/writerMessage"
    }

    //메세지  상세보기페이지
    @GetMapping("/{id}")
    suspend fun showMessageDetail(@PathVariable id: Long, model: Model): String {
        val message = messageService.getMessageById(id)
        val sender = messageService.getMemberById(message.senderId)

        // 메시지를 읽은 것으로 표시 (현재 사용자가 수신자인 경우)
        val currentUser = messageService.getCurrentUser()
        message.recipients.find { it.recipientId == currentUser.id }?.let { recipient ->
            if (recipient.readAt == null) {
                messageService.markAsRead(message.id, currentUser.id)
            }
        }

        model.addAttribute("message", message)
        model.addAttribute("sender", sender)

        return "domain/messages/messageDetail"
    }
}
