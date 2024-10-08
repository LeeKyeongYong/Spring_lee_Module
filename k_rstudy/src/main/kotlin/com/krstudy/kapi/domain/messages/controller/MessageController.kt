package com.krstudy.kapi.domain.messages.controller

import com.krstudy.kapi.domain.member.service.MemberService
import com.krstudy.kapi.domain.messages.dto.MessageRequest
import com.krstudy.kapi.domain.messages.service.MessageService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.result.view.Rendering
import org.springframework.http.MediaType
import org.springframework.web.reactive.result.view.RedirectView

@Controller
@RequestMapping("/messages")
class MessageController(
    private val messageService: MessageService
) {
    @GetMapping(produces = [MediaType.TEXT_HTML_VALUE])
    suspend fun showMessageList(// 보낸 메세지함.
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) search: String?
    ): String {
        val pageable = PageRequest.of(page - 1, size, Sort.by("sentAt").descending())
        val currentUser = messageService.getCurrentUser()

        val messagesPage = if (search.isNullOrBlank()) {
            messageService.getMessagesForUser(currentUser.id, pageable)
        } else {
            messageService.searchMessages(currentUser.id, search, pageable)
        }

        val model = mapOf(
            "messages" to messagesPage.content,
            "currentPage" to messagesPage.number + 1,
            "totalPages" to messagesPage.totalPages,
            "totalItems" to messagesPage.totalElements
        )

        return "domain/messages/messagesList"
    }

 @GetMapping("/writerForm")
    suspend fun showNewMessageForm(): String {//메세지 작성하기
        return "domain/messages/writerMessage"
    }

    //메세지  저장하기 임시페이지
    @RequestMapping("/save",method = [RequestMethod.GET, RequestMethod.POST])
    suspend fun sendNewMessage() : String   {//메세지 읽기
        println("MessageRequest: 들어오다")
        return "redirect:/messages"
    }
}
