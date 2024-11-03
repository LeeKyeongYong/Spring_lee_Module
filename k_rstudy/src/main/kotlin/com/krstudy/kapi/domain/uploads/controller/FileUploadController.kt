// FileUploadController.kt
package com.krstudy.kapi.domain.uploads.controller

import com.krstudy.kapi.domain.uploads.dto.FileUploadResponse
import com.krstudy.kapi.domain.uploads.entity.FileEntity
import com.krstudy.kapi.domain.uploads.service.FileService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/files")
class FileUploadController(
    private val fileService: FileService
) {
    @PostMapping("/upload")
    fun uploadFile(
        @RequestParam("file") files: Array<MultipartFile>,
        @RequestHeader("X-User-Id") userId: String
    ): ResponseEntity<FileUploadResponse> {
        return try {
            val uploadedFiles = fileService.uploadFiles(files, userId)
            ResponseEntity.ok(FileUploadResponse(
                message = "Files uploaded successfully",
                fileIds = uploadedFiles.map { it.id.toString() }
            ))
        } catch (e: Exception) {
            ResponseEntity.badRequest()
                .body(FileUploadResponse(
                    message = "Upload failed: ${e.message}",
                    fileIds = null
                ))
        }
    }

    @GetMapping("/user")
    fun getUserFiles(@RequestHeader("X-User-Id") userId: String): ResponseEntity<List<FileEntity>> {
        return ResponseEntity.ok(fileService.getUserFiles(userId))
    }

    @DeleteMapping("/{id}")
    fun deleteFile(
        @PathVariable id: Long,
        @RequestHeader("X-User-Id") userId: String
    ): ResponseEntity<Void> {
        fileService.deleteFile(id, userId)
        return ResponseEntity.noContent().build()
    }
}