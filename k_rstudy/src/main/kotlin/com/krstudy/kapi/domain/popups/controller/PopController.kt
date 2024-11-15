package com.krstudy.kapi.domain.popups.controller

import com.krstudy.kapi.domain.popups.service.PopupService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller

class PopController (
    private val popupService: PopupService
) {

    /**
     * 팝업 관리 목록 페이지
     */
    @GetMapping("/adm/popups")
    fun popupManagement(model: Model): String {
        val popups = popupService.getAllPopups()
        model.addAttribute("popups", popups)
        return "domain/home/adm/managementPopup"
    }

    /**
     * 팝업 생성 폼 페이지
     */
    @GetMapping("/adm/popups/create")
    fun createPopupForm(): String {
        return "domain/home/adm/createPopup"
    }
}