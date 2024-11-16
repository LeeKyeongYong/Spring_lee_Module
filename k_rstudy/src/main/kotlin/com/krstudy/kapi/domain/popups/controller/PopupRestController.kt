package com.krstudy.kapi.domain.popups.controller

import com.krstudy.kapi.domain.popups.dto.PopupCreateRequest
import com.krstudy.kapi.domain.popups.dto.PopupResponse
import com.krstudy.kapi.domain.popups.entity.DeviceType
import com.krstudy.kapi.domain.popups.exception.PopupCreationException
import com.krstudy.kapi.domain.popups.service.PopupService
import jakarta.persistence.EntityNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

/**
 * 팝업 REST API 컨트롤러
 */
@RestController
@RequestMapping("/api/popups")
class PopupRestController(
    private val popupService: PopupService
) {
    /**
     * 팝업 생성
     */
    @PostMapping
    fun createPopup(
        @RequestPart("popup") request: PopupCreateRequest,
        @RequestPart("image", required = false) image: MultipartFile?,
        @AuthenticationPrincipal userDetails: UserDetails?
    ): ResponseEntity<Any> {
        return try {
            if (userDetails == null) {
                ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(mapOf("error" to "Unauthorized"))
            } else {
                val popup = popupService.createPopup(request, image, userDetails.username)
                ResponseEntity.ok(popup)
            }
        } catch (e: Exception) {
            // 에러 로깅 추가
            println("팝업 생성 중 에러 발생: ${e.message}")
            e.printStackTrace()
            handleException(e)
        }
    }

    /**
     * 팝업 삭제 API
     */
    @DeleteMapping("/{id}")
    fun deletePopup(@PathVariable id: Long): ResponseEntity<Void> {
        popupService.deletePopup(id)
        return ResponseEntity.ok().build()
    }

    /**
     * 팝업 상태 변경 API
     */
    @PatchMapping("/{id}/status")
    fun updatePopupStatus(
        @PathVariable id: Long,
        @RequestParam status: String
    ): ResponseEntity<PopupResponse> {
        val popup = popupService.updatePopupStatus(id, status)
        return ResponseEntity.ok(popup)
    }

    /**
     * 팝업 수정 API
     */
    @PutMapping("/{id}")
    fun updatePopup(
        @PathVariable id: Long,
        @RequestPart("popup") request: PopupCreateRequest,
        @RequestPart("image", required = false) image: MultipartFile?,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<PopupResponse> {
        val popup = popupService.updatePopup(id, request, image, userDetails.username)
        return ResponseEntity.ok(popup)
    }

    /**
     * 팝업 상세 조회 API
     */
    @GetMapping("/{id}")
    fun getPopup(@PathVariable id: Long): ResponseEntity<PopupResponse> {
        val popup = popupService.getPopup(id)
        return ResponseEntity.ok(popup)
    }


    /**
     * 활성화된 팝업 조회
     */
    @GetMapping
    fun getActivePopups(
        @RequestParam deviceType: String,
        @RequestParam(required = false) page: String?
    ): ResponseEntity<List<PopupResponse>> {
        val type = DeviceType.valueOf(deviceType.uppercase())
        return ResponseEntity.ok(popupService.getActivePopups(type, page))
    }

    /**
     * 팝업 조회수 증가
     */
    @PostMapping("/{id}/view")
    fun incrementViewCount(@PathVariable id: Long): ResponseEntity<Void> {
        popupService.incrementViewCount(id)
        return ResponseEntity.ok().build()
    }

    /**
     * 팝업 클릭수 증가
     */
    @PostMapping("/{id}/click")
    fun incrementClickCount(@PathVariable id: Long): ResponseEntity<Void> {
        popupService.incrementClickCount(id)
        return ResponseEntity.ok().build()
    }

    /**
     * 예외 처리 유틸리티 메서드
     */
    private fun handleException(e: Exception): ResponseEntity<Any> {
        return when (e) {
            is PopupCreationException -> ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(mapOf("error" to e.message))

            is IllegalArgumentException -> ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(mapOf("error" to (e.message ?: "Invalid request")))

            is EntityNotFoundException -> ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(mapOf("error" to (e.message ?: "Resource not found")))

            else -> ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("error" to "An unexpected error occurred"))
        }
    }

}