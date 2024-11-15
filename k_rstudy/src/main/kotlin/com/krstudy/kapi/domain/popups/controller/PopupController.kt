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
 * 팝업 관리 컨트롤러 (관리자용)
 */
@Controller
@RequestMapping("/adm/popups")
class PopupController(
    private val popupService: PopupService
) {
    /**
     * 팝업 관리 페이지
     */
    @GetMapping
    fun popupManagement(model: Model): String {
        val popups = popupService.getAllPopups()
        model.addAttribute("popups", popups)
        return "domain/home/adm/managementPopup"
    }

    /**
     * 팝업 생성 폼
     */
    @GetMapping("/create")
    fun createPopupForm(model: Model): String {
        return "domain/home/adm/createPopup"
    }
}

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
            handleException(e)
        }
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