package com.krstudy.kapi.domain.uploads.controller

import com.krstudy.kapi.domain.uploads.dto.FileStatus
import com.krstudy.kapi.domain.uploads.dto.FileUploadResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

@RestController
@RequestMapping("/api/v1/files")
class FileUploadController (
    @Value("\${file.upload-dir.windows}") private val uploadDir: String
) {

    @PostMapping("/upload")
    fun uploadFile(@RequestParam("file") files: Array<MultipartFile>): ResponseEntity<FileUploadResponse> {
        return try {
            // 각 파일 처리
            files.forEach { file ->
                val fileId = UUID.randomUUID().toString()
                val filePath = Path.of(uploadDir, fileId)

                // 디렉토리가 없으면 생성
                Files.createDirectories(filePath.parent)

                // 파일 저장
                file.transferTo(filePath)
            }

            ResponseEntity.ok(FileUploadResponse(
                fileId = UUID.randomUUID().toString(),
                message = "Files uploaded successfully"
            ))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(FileUploadResponse(null, "Upload failed: ${e.message}"))
        }
    }

    @GetMapping("/{fileId}/status")
    fun getFileStatus(@PathVariable fileId: String): ResponseEntity<FileStatus> {
        val filePath = Path.of(uploadDir, fileId)
        return if (Files.exists(filePath)) {
            ResponseEntity.ok(FileStatus(fileId, "UPLOADED"))
        } else {
            ResponseEntity.notFound().build()
        }
    }
}