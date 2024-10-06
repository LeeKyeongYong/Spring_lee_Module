package com.krstudy.kapi.domain.messages.controller

import org.springframework.web.bind.annotation.*
import org.springframework.http.MediaType // Add this import
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import com.krstudy.kapi.domain.messages.service.MessageService
import com.krstudy.kapi.domain.messages.dto.*
import com.krstudy.kapi.domain.member.dto.MemberDto
import com.krstudy.kapi.domain.member.service.MemberService

@RestController
@RequestMapping("/api/messages")
class ApiMessageController(
    private val messageService: MessageService,
    private val memberService: MemberService
) {
    @PostMapping
    suspend fun sendMessage(@RequestBody messageRequest: MessageRequest): ResponseEntity<MessageResponse> {
        val currentUser = memberService.getCurrentUser()
        val message = messageRequest.toMessage(currentUser)
        val result = messageService.sendMessage(message)
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(MessageResponse.fromMessage(result))
    }

    @GetMapping("/unread/{memberId}")
    suspend fun getUnreadCount(@PathVariable memberId: Long): ResponseEntity<UnreadCountResponse> {
        val count = messageService.getUnreadMessagesCount(memberId)
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(UnreadCountResponse(count))
    }

    @GetMapping("/search-users")
    suspend fun searchUsers(@RequestParam searchTerm: String): ResponseEntity<List<MemberDto>> {
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(messageService.searchUsers(searchTerm))
    }
}