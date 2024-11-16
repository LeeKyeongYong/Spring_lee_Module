package com.krstudy.kapi.domain.popups.controller

import com.krstudy.kapi.domain.popups.dto.*
import com.krstudy.kapi.domain.popups.enums.PopupStatus
import com.krstudy.kapi.domain.popups.service.PopupService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@PreAuthorize("hasRole('POPUP_ADMIN')")
@RestController
@RequestMapping("/api/admin/popups")
class PopupAdminController(
    private val popupService: PopupService
) {
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
        @RequestBody settings: PopupCloneSettingsDto,
        @AuthenticationPrincipal user: UserDetails
    ): ResponseEntity<PopupResponse> {
        return ResponseEntity.ok(popupService.clonePopup(id, settings, user.username))
    }

    @GetMapping("/preview/{id}")
    fun previewPopup(@PathVariable id: Long, model: Model): String {
        val popup = popupService.getPopup(id)
        model.addAttribute("popup", popup)
        return "admin/popup/template-preview"
    }
}