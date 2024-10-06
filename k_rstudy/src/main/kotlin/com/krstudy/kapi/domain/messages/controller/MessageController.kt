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

@Controller
@RequestMapping("/messages")
class MessageController(
    private val messageService: MessageService,
    private val memberService: MemberService
) {
    @GetMapping(produces = [MediaType.TEXT_HTML_VALUE])
    suspend fun showMessageList(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) search: String?
    ): String {
        val pageable = PageRequest.of(page - 1, size, Sort.by("sentAt").descending())
        val currentUser = memberService.getCurrentUser()

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

//    @GetMapping("/new", produces = [MediaType.TEXT_HTML_VALUE])
 @GetMapping("/new")
    suspend fun showNewMessageForm(): String {
        return "domain/messages/newMessage"
    }

    @PostMapping("/new")
    suspend fun sendNewMessage(@ModelAttribute messageRequest: MessageRequest): String {
        val currentUser = memberService.getCurrentUser()
        val message = messageRequest.toMessage(currentUser)
        messageService.sendMessage(message)
        return "redirect:/messages"
    }
}