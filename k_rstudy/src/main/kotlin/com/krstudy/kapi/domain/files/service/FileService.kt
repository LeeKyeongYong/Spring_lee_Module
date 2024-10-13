package com.krstudy.kapi.domain.files.service

import com.krstudy.kapi.domain.files.entity.FileEntity
import com.krstudy.kapi.domain.files.repository.FileRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@Service
class FileService(private val fileRepository: FileRepository) {

    @Value("\${file.upload-dir.windows}")
    private lateinit var uploadDir: String // application.yml에서 경로를 주입받음

    fun saveFile(file: MultipartFile, relatedEntityId: Long, entityType: String): FileEntity {
        val originalFilename = file.originalFilename ?: throw IllegalArgumentException("Filename cannot be null")
        val storedFilename = "${System.currentTimeMillis()}_$originalFilename" // 저장 파일 이름 생성
        val path: Path = Paths.get(uploadDir, storedFilename)

        Files.copy(file.inputStream, path) // 파일 저장

        val fileEntity = FileEntity(originalFilename = originalFilename, storedFilename = storedFilename, relatedEntityId = relatedEntityId, entityType = entityType)
        return fileRepository.save(fileEntity) // 파일 정보 DB에 저장
    }
}