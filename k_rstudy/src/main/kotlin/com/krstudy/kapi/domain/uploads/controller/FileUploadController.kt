package com.krstudy.kapi.domain.uploads.controller

import com.krstudy.kapi.domain.uploads.dto.FileStatus
import com.krstudy.kapi.domain.uploads.dto.FileUploadResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.*
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
    fun uploadFile(@RequestPart("file") filePart: FilePart): Mono<FileUploadResponse> {
        return Mono.fromCallable {
            val fileId = UUID.randomUUID().toString()
            val filePath = Path.of(uploadDir, fileId)

            // 파일을 서버 로컬 디렉터리에 저장
            filePart.transferTo(filePath.toFile()).subscribeOn(Schedulers.boundedElastic()).block()
            FileUploadResponse(fileId, "File upload successful")
        }
            .onErrorResume { e ->
                Mono.just(FileUploadResponse(null, "Upload failed: ${e.message}"))
            }
    }

    @GetMapping("/{fileId}/status")
    fun getFileStatus(@PathVariable fileId: String): Mono<FileStatus> {
        return Mono.fromCallable {
            val filePath = Path.of(uploadDir, fileId)
            if (Files.exists(filePath)) {
                FileStatus(fileId, "UPLOADED")
            } else {
                throw ResponseStatusException(HttpStatus.NOT_FOUND, "File not found")
            }
        }
            .subscribeOn(Schedulers.boundedElastic())
    }
}