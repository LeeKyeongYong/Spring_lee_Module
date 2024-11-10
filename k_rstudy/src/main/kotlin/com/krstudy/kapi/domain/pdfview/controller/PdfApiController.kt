package com.krstudy.kapi.domain.pdfview.controller

import com.krstudy.kapi.domain.pdfview.service.PdfService
import com.krstudy.kapi.domain.uploads.entity.FileEntity
import com.krstudy.kapi.global.Security.datas.AuthenticationFacade
import com.krstudy.kapi.global.exception.GlobalException
import com.krstudy.kapi.global.https.RespData
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
    @PreAuthorize("isAuthenticated()")
    fun uploadPdf(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("title") title: String
    ): ResponseEntity<RespData<FileEntity>> {
        logger.info("File upload started - fileName: ${file.originalFilename}, title: $title")

        return try {
            val currentUser = authenticationFacade.getCurrentUser()
                ?: throw UnauthorizedException("인증된 사용자 정보를 찾을 수 없습니다.")

            validateFile(file)

            val uploadedFile = pdfService.uploadPdf(file, currentUser.username, title)
            logger.info("File upload successful - fileId: ${uploadedFile.id}")

            ResponseEntity.ok(
                RespData.of(
                resultCode = "200",
                message = "파일이 성공적으로 업로드되었습니다.",
                data = uploadedFile
            ))
        } catch (e: Exception) {
            logger.error("File upload failed", e)
            when (e) {
                is UnauthorizedException -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                is InvalidFileException -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                else -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            }.body(RespData.of(
                resultCode = "500",
                message = e.message ?: "파일 업로드 중 오류가 발생했습니다.",
                data = null
            ))
        }
    }

    private fun validateFile(file: MultipartFile) {
        when {
            file.isEmpty -> throw InvalidFileException("파일이 비어있습니다.")
            file.originalFilename.isNullOrEmpty() -> throw InvalidFileException("파일명이 없습니다.")
            !file.contentType?.equals("application/pdf", ignoreCase = true)!! ->
                throw InvalidFileException("PDF 파일만 업로드 가능합니다.")
            file.size > 10_485_760 -> // 10MB limit
                throw InvalidFileException("파일 크기는 10MB를 초과할 수 없습니다.")
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
            is GlobalException -> {  // GlobalException을 처리하는 예시
                logger.error("Global error: ${e.message}", e)
                ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorResponse("잘못된 요청", e.message))
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
