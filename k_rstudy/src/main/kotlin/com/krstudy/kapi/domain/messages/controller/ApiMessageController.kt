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
import com.krstudy.kapi.domain.messages.entity.Message
import com.krstudy.kapi.domain.messages.entity.MessageRecipient
import jakarta.servlet.http.HttpServletRequest
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/messages")
class ApiMessageController(
    private val messageService: MessageService

) {
    private val logger = LoggerFactory.getLogger(ApiMessageController::class.java)
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun showMessageList(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) search: String?
        , httpServletRequest: HttpServletRequest
    ): ResponseEntity<Map<String, Any>> {
        logger.info("Received request with Content-Type: ${httpServletRequest.contentType}")
        val pageable = PageRequest.of(page - 1, size, Sort.by("createDate").descending())
        val currentUser = messageService.getCurrentUser()

        // 로그인한 유저가 보낸 메시지만 가져오도록 수정
        val messagesPage = if (search.isNullOrBlank()) {
            messageService.getSentMessages(currentUser.id, pageable)
        } else {
            messageService.searchSentMessages(currentUser.id, search, pageable)
        }

        // 수신자 이름 포맷 변경
        val formattedMessages = messagesPage.content.map { message ->
            MessageResponse(
                id = message.id,
                content = message.content,
                title = message.title,
                senderId = message.senderId,
                recipients = message.recipients.map {
                    RecipientDto(
                        recipientId = it.recipientId,
                        recipientName = "${it.recipientName} (${it.recipientUserId})",
                        recipientUserId = it.recipientUserId
                    )
                },
                sentAt = message.sentAt,
                readAt = message.recipients.find { it.recipientId == currentUser.id }?.readAt // 현재 사용자 기준으로 읽음 상태 처리
            )
        }

        val response = mapOf(
            "messages" to formattedMessages,
            "currentPage" to messagesPage.number + 1,
            "totalPages" to messagesPage.totalPages,
            "totalItems" to messagesPage.totalElements
        )

        return ResponseEntity.ok(response)
    }

    @GetMapping("/search-users", consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun searchUsers(@RequestParam(required = false, defaultValue = "") searchUsername: String): ResponseEntity<List<MemberDto>> {
        println("검색어: $searchUsername") // 검색어 출력

        val users = if (searchUsername.isNullOrBlank()) {
            messageService.getAllUsers()  // 검색어 없으면 전체 유저 반환

        } else {
            messageService.searchUsers(searchUsername) // 검색어 있을 때 해당 유저 검색
        }

        println("검색된 사용자 수: ${users.size}") // 검색된 사용자 수 출력
        println("검색된 사용자 목록: $users") // 전체 사용자 목록 출력

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(users)
    }

    @PostMapping("/save")
    suspend fun saveMessage(@RequestBody request: MessageSaveRequest): ResponseEntity<Message> {
        val currentUser = messageService.getCurrentUser()

        val message = Message(
            content = request.content,
            senderId = currentUser.id,
            sentAt = LocalDateTime.now(),
            title = request.title
        )

        // 수신자 정보 설정
        request.recipients.forEach { recipientDto ->
            message.recipients.add(
                MessageRecipient(
                    message = message,
                    recipientId = recipientDto.recipientId, // 수정된 부분
                    recipientName = recipientDto.recipientName, // 수정된 부분
                    recipientUserId = recipientDto.recipientUserId // 추가된 부분
                )
            )
        }

        val savedMessage = messageService.sendMessage(message)
        return ResponseEntity.ok(savedMessage)
    }

    @GetMapping("/unread-count")
    fun getUnreadCount(): ResponseEntity<UnreadCountResponse> = runBlocking {
        val currentUser = messageService.getCurrentUser()
        val count = messageService.getUnreadMessagesCount(currentUser.id)
        ResponseEntity.ok(UnreadCountResponse(count))
    }

    @GetMapping("/received", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun showReceivedMessageList(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) search: String?
    ): ResponseEntity<Map<String, Any>> {
        val pageable = PageRequest.of(page - 1, size, Sort.by("createDate").descending())
        val currentUser = messageService.getCurrentUser()

        val messagesPage = if (search.isNullOrBlank()) {
            messageService.getMessagesForRecipientUserId(currentUser.userid, pageable) // 변경된 메서드 호출
        } else {
            messageService.searchMessagesForRecipientUserId(currentUser.id, search, pageable) // 검색 메서드도 수정
        }

        val formattedMessages = messagesPage.content.map { message ->
            MessageResponse.fromMessage(message) // 메시지 응답 형식으로 변환
        }

        val response = mapOf(
            "messages" to formattedMessages,
            "currentPage" to messagesPage.number + 1,
            "totalPages" to messagesPage.totalPages,
            "totalItems" to messagesPage.totalElements
        )

        return ResponseEntity.ok(response)
    }



    // 메시지 읽음 처리
    @PostMapping("/{messageId}/read")
    suspend fun markMessageAsRead(@PathVariable messageId: Long): ResponseEntity<UnreadCountResponse> {
        val currentUser = messageService.getCurrentUser()
        messageService.markAsRead(messageId, currentUser.id)

        // 읽음 처리 후 새로운 안 읽은 메시지 수 반환
        val newUnreadCount = messageService.getUnreadMessagesCount(currentUser.id)
        return ResponseEntity.ok(UnreadCountResponse(newUnreadCount))
    }


    @GetMapping("/unread/{memberId}")
    suspend fun getUnreadCount(@PathVariable memberId: Long): ResponseEntity<UnreadCountResponse> {
        val currentUser = messageService.getCurrentUser()
        val count = messageService.getUnreadMessagesCount(currentUser.id)
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(UnreadCountResponse(count))
    }

}
