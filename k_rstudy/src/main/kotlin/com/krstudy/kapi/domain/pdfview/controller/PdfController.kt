package com.krstudy.kapi.domain.pdfview.controller

import com.krstudy.kapi.domain.uploads.service.FileServiceImpl
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/uploadPdf")
class PdfController(
    private val fileService: FileServiceImpl
) {
    @GetMapping
    fun listDocuments(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        model: Model
    ): String {
        val documents = fileService.getAllActivePdfFiles(page - 1, size)
        model.addAttribute("documents", documents.content)
        model.addAttribute("page", page)
        model.addAttribute("totalPages", documents.totalPages)
        return "domain/upload/pdfMain"
    }
}