package com.krstudy.kapi.domain.messages.controller

import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import com.krstudy.kapi.domain.messages.service.MessageService
import com.krstudy.kapi.domain.messages.dto.*
import com.krstudy.kapi.domain.member.dto.MemberDto

@RestController
@RequestMapping("/api/messages")
class ApiMessageController(
    private val messageService: MessageService
) {
    @PostMapping
    suspend fun sendMessage(@RequestBody message: MessageRequest): ResponseEntity<MessageResponse> {
        val result = messageService.sendMessage(message.toMessage())
        return ResponseEntity.ok(MessageResponse.fromMessage(result))
    }

    @GetMapping("/unread/{memberId}")
    suspend fun getUnreadCount(@PathVariable memberId: String): ResponseEntity<UnreadCountResponse> {
        val count = messageService.getUnreadMessagesCount(memberId)
        return ResponseEntity.ok(UnreadCountResponse(count))
    }

    @GetMapping("/search-users")
    suspend fun searchUsers(
        @RequestParam searchTerm: String
    ): ResponseEntity<List<MemberDto>> {
        return ResponseEntity.ok(messageService.searchUsers(searchTerm))
    }
}