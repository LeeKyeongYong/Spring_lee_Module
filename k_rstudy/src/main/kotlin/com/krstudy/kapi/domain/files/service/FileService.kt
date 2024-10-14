package com.krstudy.kapi.domain.files.service

import com.krstudy.kapi.domain.files.entity.FileEntity
import com.krstudy.kapi.domain.files.repository.FileRepository
import com.krstudy.kapi.global.exception.FileSaveException
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*

@Service
class FileService(private val fileRepository: FileRepository) {

    @Value("\${file.upload-dir.windows}")
    private var windowsUploadDir: String? = null

    @Value("\${file.upload-dir.linux}")
    private var linuxUploadDir: String? = null

    private lateinit var uploadDir: String

    @PostConstruct
    fun init() {
        uploadDir = when {
            !windowsUploadDir.isNullOrBlank() -> windowsUploadDir!!
            !linuxUploadDir.isNullOrBlank() -> linuxUploadDir!!
            else -> System.getProperty("java.io.tmpdir")
        }
        println("Upload directory initialized: $uploadDir")

        val directory = File(uploadDir)
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                println("Created directory: ${directory.absolutePath}")
            } else {
                throw IllegalStateException("Failed to create directory: ${directory.absolutePath}")
            }
        }

        if (!directory.canWrite()) {
            throw IllegalStateException("No write permission for directory: ${directory.absolutePath}")
        }
    }

    fun saveFile(file: MultipartFile, relatedEntityId: Long, entityType: String): FileEntity {
        val originalFilename = file.originalFilename ?: throw IllegalArgumentException("Filename cannot be null")
        val storedFilename = "${UUID.randomUUID()}_$originalFilename"  // UUID 사용으로 파일명 충돌 방지
        val path: Path = Paths.get(uploadDir, storedFilename)

        try {
            Files.copy(file.inputStream, path, StandardCopyOption.REPLACE_EXISTING)
        } catch (e: IOException) {
            throw FileSaveException("Failed to save file: ${e.message}", e)  // 사용자 정의 예외
        }

        val fileEntity = FileEntity(
            originalFilename = originalFilename,
            storedFilename = storedFilename,
            relatedEntityId = relatedEntityId,
            entityType = entityType
        )
        return fileRepository.save(fileEntity)
    }
}