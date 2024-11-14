package com.krstudy.kapi.domain.pdfview.controller

import com.krstudy.kapi.domain.uploads.service.FileServiceImpl
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/uploadPdf")
class PdfController (
    private val fileService: FileServiceImpl
){
    @GetMapping
    fun listDocuments(
        @RequestParam(defaultValue = "1") page: Int,
        model: Model
    ): String {
        val documents = fileService.getAllActivePdfFiles(page - 1)
        model.addAttribute("documents", documents)
        model.addAttribute("page", page)
        return "domain/upload/pdfMain"
    }
}