package com.krstudy.kapi.domain.pdfview.controller

import com.krstudy.kapi.domain.pdfview.service.PdfService
import com.krstudy.kapi.domain.uploads.entity.FileEntity
import org.springframework.http.ResponseEntity
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import java.util.UUID

@RestController
@RequestMapping("/api/pdf")
class PdfApiController(
    private val pdfService: PdfService
) {
    @PostMapping("/upload")
    fun uploadPdf(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("title") title: String,
        @RequestParam("userId") userId: String
    ): ResponseEntity<FileEntity> {
        val uploadedFile = pdfService.uploadPdf(file, userId, title)
        return ResponseEntity.ok(uploadedFile)
    }

    @GetMapping("/{fileId}/preview/{pageNum}")
    fun getPdfPreview(
        @PathVariable fileId: Long,
        @PathVariable pageNum: Int
    ): ResponseEntity<Resource> {
        val resource = pdfService.getPdfPreview(fileId, pageNum)
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_PNG)
            .body(resource)
    }

    @GetMapping("/{fileId}/download")
    fun downloadPdf(@PathVariable fileId: Long): ResponseEntity<Resource> {
        val resource = pdfService.downloadPdf(fileId)
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_PDF)
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"${UUID.randomUUID()}.pdf\""
            )
            .body(resource)
    }

    @GetMapping("/{fileId}")
    fun viewPdf(@PathVariable fileId: Long, model: Model): String {
        model.addAttribute("fileId", fileId)
        return "pdf-viewer"
    }

}
