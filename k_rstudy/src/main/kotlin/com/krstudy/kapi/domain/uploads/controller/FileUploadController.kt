package com.krstudy.kapi.domain.uploads.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import com.krstudy.kapi.domain.uploads.dto.FileUploadResponse
import com.krstudy.kapi.domain.uploads.entity.FileEntity
import com.krstudy.kapi.domain.uploads.service.FileServiceImpl
import jakarta.persistence.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

@RestController
@RequestMapping("/api/v1/files")
class FileUploadController(
    @Autowired
    @Qualifier("uploadFileService")
    private val fileService: FileServiceImpl
) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(FileServiceImpl::class.java)
    }

    @PostMapping("/upload")
    fun uploadFile(
        @RequestParam("file") files: Array<MultipartFile>,
        @RequestHeader("X-User-Id") userId: String
    ): ResponseEntity<FileUploadResponse> {
        logger.debug("Uploading files for user: $userId") // 로그 추가
        return try {
            val userId = URLDecoder.decode(userId, StandardCharsets.UTF_8.name())
            val uploadedFiles = fileService.uploadFiles(files, userId)
            ResponseEntity.ok(
                FileUploadResponse(
                    message = "Files uploaded successfully",
                    fileIds = uploadedFiles.map { it.id.toString() }
                )
            )
        } catch (e: EntityNotFoundException) {
            logger.error("Upload failed: User not found with ID: $userId", e) // 에러 로그 추가
            ResponseEntity.badRequest()
                .body(
                    FileUploadResponse(
                        message = "Upload failed: User not found with ID: $userId",
                        fileIds = null
                    )
                )
        } catch (e: Exception) {
            logger.error("Upload failed: ${e.message}", e) // 에러 로그 추가
            ResponseEntity.badRequest()
                .body(
                    FileUploadResponse(
                        message = "Upload failed: ${e.message}",
                        fileIds = null
                    )
                )
        }
    }

    @GetMapping("/download/{id}")
    fun downloadFile(@PathVariable id: Long): ResponseEntity<Resource> {
        val file = fileService.getFileById(id)
        val path = Paths.get(file.filePath)
        val resource = InputStreamResource(Files.newInputStream(path))

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(file.contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"${file.originalFileName}\"")
            .body(resource)
    }

//    @GetMapping("/view/{id}")
//    fun viewFile(@PathVariable id: Long): ResponseEntity<Resource> {
//        val file = fileService.getFileById(id)
//        val path = Paths.get(file.filePath)
//        val resource = InputStreamResource(Files.newInputStream(path))
//
//        return ResponseEntity.ok()
//            .contentType(MediaType.parseMediaType(file.contentType))
//            .body(resource)
//    }

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