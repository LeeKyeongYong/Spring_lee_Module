package com.krstudy.kapi.domain.messages.controller

import com.krstudy.kapi.domain.member.service.MemberService
import com.krstudy.kapi.domain.messages.dto.MessageRequest
import com.krstudy.kapi.domain.messages.service.MessageService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.result.view.Rendering

@RestController
@RequestMapping("/messages")
class MessageController(
    private val messageService: MessageService,
    private val memberService: MemberService
) {
    @GetMapping
    suspend fun showMessageList(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) search: String?
    ): Rendering {
        val pageable = PageRequest.of(page - 1, size, Sort.by("sentAt").descending())
        val currentUser = memberService.getCurrentUser()

        val messagesPage = if (search.isNullOrBlank()) {
            messageService.getMessagesForUser(currentUser.id, pageable)
        } else {
            messageService.searchMessages(currentUser.id, search, pageable)
        }

        return Rendering.view("domain/messages/messagesList")
            .modelAttribute("messages", messagesPage.content)
            .modelAttribute("currentPage", messagesPage.number + 1)
            .modelAttribute("totalPages", messagesPage.totalPages)
            .modelAttribute("totalItems", messagesPage.totalElements)
            .build()
    }

    @GetMapping("/new")
    suspend fun showNewMessageForm(): Rendering {
        return Rendering.view("domain/messages/newMessage")
            .modelAttribute("messageRequest", MessageRequest("", 0))
            .build()
    }

    @PostMapping("/new")
    suspend fun sendNewMessage(@ModelAttribute messageRequest: MessageRequest): Rendering {
        val currentUser = memberService.getCurrentUser()
        val message = messageRequest.toMessage(currentUser)
        messageService.sendMessage(message)
        return Rendering.redirectTo("/messages").build()
    }
}