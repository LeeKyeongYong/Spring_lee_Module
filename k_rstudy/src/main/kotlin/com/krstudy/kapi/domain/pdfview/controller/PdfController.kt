package com.krstudy.kapi.domain.pdfview.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class PdfController {

    @GetMapping("/uploadPdf")
    fun uploadPdfForm(): String {
        return "domain/upload/pdfMain"
    }

    @GetMapping("/viewPdf")
    fun viewPdfForm(): String {
        return "domain/upload/pdfView"
    }
}