package com.krstudy.kapi.domain.popups.controller

import com.krstudy.kapi.domain.popups.enums.PopupStatus
import com.krstudy.kapi.domain.popups.service.PopupService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@PreAuthorize("hasRole('POPUP_ADMIN')")
@RestController
@RequestMapping("/api/admin/popups")
class PopupAdminController(
    private val popupService: PopupService
) {
    @PostMapping("/{id}/activate")
    fun activatePopup(@PathVariable id: Long): ResponseEntity<Void> {
        popupService.changePopupStatus(id, PopupStatus.ACTIVE)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/{id}/deactivate")
    fun deactivatePopup(@PathVariable id: Long): ResponseEntity<Void> {
        popupService.changePopupStatus(id, PopupStatus.INACTIVE)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/{id}/history")
    fun getPopupHistory(@PathVariable id: Long): ResponseEntity<List<PopupHistoryResponse>> {
        return ResponseEntity.ok(popupService.getPopupHistory(id))
    }

    // 대량 작업 지원
    @PostMapping("/bulk-update")
    fun bulkUpdate(@RequestBody updates: List<PopupBulkUpdate>)

    // 팝업 복제 시 설정 상속
    @PostMapping("/{id}/clone-with-settings")
    fun cloneWithSettings(@PathVariable id: Long,
                          @RequestBody settings: CloneSettings)

    // 변경 이력 추적
    @GetMapping("/{id}/history")
    fun getChangeHistory(@PathVariable id: Long): List<PopupChangeLog>

    @GetMapping("/preview/{id}")
    fun previewPopup(@PathVariable id: Long, model: Model): String {
        val popup = popupService.getPopup(id)
        model.addAttribute("popup", popup)
        return "admin/popup/template-preview"
    }
}