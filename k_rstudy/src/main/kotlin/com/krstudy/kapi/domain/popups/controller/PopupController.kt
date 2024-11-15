package com.krstudy.kapi.domain.popups.controller

import com.krstudy.kapi.domain.popups.dto.PopupCreateRequest
import com.krstudy.kapi.domain.popups.dto.PopupResponse
import com.krstudy.kapi.domain.popups.entity.DeviceType
import com.krstudy.kapi.domain.popups.service.PopupService
import com.opencsv.bean.util.OpencsvUtils.handleException
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
        try {
            if (userDetails == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(mapOf("error" to "Unauthorized"))
            }

            val popup = popupService.createPopup(request, image, userDetails.username)
            return ResponseEntity.ok(popup)
        } catch (e: Exception) {
            return handleException(e)
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
}