package com.krstudy.kapi.domain.pdfview.controller

import com.krstudy.kapi.domain.pdfview.service.PdfService
import com.krstudy.kapi.domain.uploads.entity.FileEntity
import com.krstudy.kapi.global.Security.datas.AuthenticationFacade
import jakarta.persistence.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import java.util.UUID

@RestController
@RequestMapping("/api/pdf")
class PdfApiController(
    private val pdfService: PdfService,
    private val authenticationFacade: AuthenticationFacade  // 인증 처리를 위한 Facade 추가
) {
    private val logger = LoggerFactory.getLogger(PdfApiController::class.java)
    @PostMapping("/upload")
    @PreAuthorize("isAuthenticated()")  // 인증된 사용자만 접근 가능
    fun uploadPdf(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("title") title: String
    ): ResponseEntity<*> {
        logger.info("File upload started - fileName: ${file.originalFilename}")

        return try {
            // 현재 인증된 사용자 정보 가져오기
            val currentUser = authenticationFacade.getCurrentUser()
                ?: throw UnauthorizedException("인증된 사용자 정보를 찾을 수 없습니다.")

            // 파일 유효성 검사
            validateFile(file)

            val uploadedFile = pdfService.uploadPdf(file, currentUser.username, title)
            logger.info("File upload successful - fileId: ${uploadedFile.id}")
            ResponseEntity.ok(uploadedFile)

        } catch (e: Exception) {
            handleUploadException(e)
        }
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

    private fun validateFile(file: MultipartFile) {
        if (file.isEmpty) {
            throw InvalidFileException("파일이 비어있습니다.")
        }
        if (!file.contentType?.equals("application/pdf", ignoreCase = true)!!) {
            throw InvalidFileException("PDF 파일만 업로드 가능합니다.")
        }
    }

    private fun handleUploadException(e: Exception): ResponseEntity<*> {
        return when (e) {
            is UnauthorizedException -> {
                logger.error("Authentication error: ${e.message}", e)
                ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse("인증 오류", e.message))
            }
            is InvalidFileException -> {
                logger.error("Invalid file: ${e.message}", e)
                ResponseEntity.badRequest()
                    .body(ErrorResponse("파일 오류", e.message))
            }
            is EntityNotFoundException -> {
                logger.error("Entity not found: ${e.message}", e)
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponse("찾을 수 없음", e.message))
            }
            else -> {
                logger.error("Unexpected error: ${e.message}", e)
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ErrorResponse("서버 오류", "파일 업로드 중 오류가 발생했습니다."))
            }
        }
    }

    // 에러 응답을 위한 데이터 클래스
    data class ErrorResponse(
        val error: String,
        val message: String?
    )

    // 커스텀 예외 클래스들
    class UnauthorizedException(message: String) : RuntimeException(message)
    class InvalidFileException(message: String) : RuntimeException(message)

}
