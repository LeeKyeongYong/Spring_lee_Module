package com.krstudy.kapi.domain.messages.controller

import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.*
import org.springframework.http.MediaType // Add this import
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import com.krstudy.kapi.domain.messages.service.MessageService
import com.krstudy.kapi.domain.messages.dto.*
import com.krstudy.kapi.domain.member.dto.MemberDto
import com.krstudy.kapi.domain.member.service.MemberService
import org.springframework.data.domain.PageRequest

@RestController
@RequestMapping("/api/messages")
class ApiMessageController(
    private val messageService: MessageService,
    private val memberService: MemberService
) {
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun showMessageList(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) search: String?
    ): ResponseEntity<Map<String, Any>> { // ResponseEntity로 감싸기
        val pageable = PageRequest.of(page - 1, size, Sort.by("sentAt").descending())
        val currentUser = messageService.getCurrentUser()

        val messagesPage = if (search.isNullOrBlank()) {
            messageService.getMessagesForUser(currentUser.id, pageable)
        } else {
            messageService.searchMessages(currentUser.id, search, pageable)
        }

        val response = mapOf(
            "messages" to messagesPage.content,
            "currentPage" to messagesPage.number + 1,
            "totalPages" to messagesPage.totalPages,
            "totalItems" to messagesPage.totalElements
        )

        return ResponseEntity.ok(response) // 200 OK 응답
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