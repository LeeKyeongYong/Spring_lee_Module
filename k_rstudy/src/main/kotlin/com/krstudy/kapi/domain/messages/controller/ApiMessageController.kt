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
import org.springframework.data.domain.PageRequest
import java.time.LocalDateTime

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
            sentAt = LocalDateTime.now()
        )

        // 수신자 정보 설정
        request.recipients.forEach { recipientDto ->
            message.recipients.add(
                MessageRecipient(
                    message = message,
                    recipientId = recipientDto.recipientId, // 수정된 부분
                    recipientName = recipientDto.recipientName // 수정된 부분
                )
            )
        }

        val savedMessage = messageService.sendMessage(message)
        return ResponseEntity.ok(savedMessage)
    }


    //4. 받은 페이지 counting
    //5. 받은 페이지함 불러오기..(로그인한 사람..)
    
    //6. 선택하면 읽음처리와 카운팅 처리..
    //7. 상세보기 기능추가
    //8. 슬라이드식 팝업 5개 보여주고 6개만
    //9. 잔처리..
    //10. 상세보기..누가 읽었는지.. 등등

}