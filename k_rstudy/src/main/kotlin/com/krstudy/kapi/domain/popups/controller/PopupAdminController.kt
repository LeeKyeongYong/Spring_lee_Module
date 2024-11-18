package com.krstudy.kapi.domain.popups.controller

import com.krstudy.kapi.domain.popups.dto.*
import com.krstudy.kapi.domain.popups.enums.PopupStatus
import com.krstudy.kapi.domain.popups.service.PopupService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.slf4j.LoggerFactory

@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@RestController
@RequestMapping("/api/admin/popups", produces = [MediaType.APPLICATION_JSON_VALUE])
class PopupAdminController(
    private val popupService: PopupService
) {

    companion object {
        private val logger = LoggerFactory.getLogger(PopupAdminController::class.java)
    }

    @PostMapping("/{id}/activate")
    fun activatePopup(
        @PathVariable id: Long,
        @AuthenticationPrincipal user: UserDetails
    ): ResponseEntity<Void> {
        popupService.changePopupStatus(id, PopupStatus.ACTIVE, user.username)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/{id}/deactivate")
    fun deactivatePopup(
        @PathVariable id: Long,
        @AuthenticationPrincipal user: UserDetails
    ): ResponseEntity<Void> {
        popupService.changePopupStatus(id, PopupStatus.INACTIVE, user.username)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/{id}/history")
    fun getPopupHistory(@PathVariable id: Long): ResponseEntity<List<PopupHistoryDto>> {
        return ResponseEntity.ok(popupService.getPopupHistory(id))
    }

    @PostMapping("/bulk-update")
    fun bulkUpdate(
        @RequestBody updates: List<PopupBulkUpdateDto>,
        @AuthenticationPrincipal user: UserDetails
    ): ResponseEntity<List<PopupResponse>> {
        return ResponseEntity.ok(popupService.bulkUpdate(updates, user.username))
    }

    @PostMapping("/{id}/clone")
    fun clonePopup(
        @PathVariable id: Long,
        @AuthenticationPrincipal user: UserDetails  // 현재 사용자 정보 주입
    ): ResponseEntity<PopupResponse> {
        logger.info("Current user authorities: ${user.authorities}")  // 권한 로그 출력
        return try {
            val clonedPopup = popupService.clonePopup(id)
            ResponseEntity.ok(clonedPopup)
        } catch (e: Exception) {
            logger.error("Clone popup error", e)  // 에러 로깅
            ResponseEntity.badRequest().body(null)
        }
    }
}