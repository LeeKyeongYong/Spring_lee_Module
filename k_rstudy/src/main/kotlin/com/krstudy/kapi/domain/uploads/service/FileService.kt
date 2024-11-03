package com.krstudy.kapi.domain.uploads.service

import com.krstudy.kapi.domain.uploads.entity.FileEntity
import com.krstudy.kapi.domain.uploads.exception.FileUploadException
import com.krstudy.kapi.domain.uploads.repository.FileRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.security.MessageDigest
import java.util.*
import jakarta.persistence.EntityNotFoundException

@Service
class FileService(
    private val fileRepository: FileRepository,
    private val uploadDir: String
) {
    @Transactional
    fun uploadFiles(files: Array<MultipartFile>, uploadedBy: String): List<FileEntity> {
        return files.map { file ->
            uploadSingleFile(file, uploadedBy)
        }
    }

    private fun uploadSingleFile(file: MultipartFile, uploadedBy: String): FileEntity {
        try {
            val fileExtension = file.originalFilename?.substringAfterLast(".")
                ?: throw IllegalArgumentException("Invalid file extension")
            val storedFileName = "${UUID.randomUUID()}.$fileExtension"
            val filePath = Path.of(uploadDir, storedFileName)

            // 디렉토리 생성
            Files.createDirectories(filePath.parent)

            // 파일 저장
            Files.copy(file.inputStream, filePath)

            // 체크섬 생성
            val checksum = calculateChecksum(file)

            // DB에 파일 정보 저장
            val fileEntity = FileEntity(
                originalFileName = file.originalFilename ?: "unknown",
                storedFileName = storedFileName,
                filePath = filePath.toString(),
                fileSize = file.size,
                fileType = fileExtension,
                contentType = file.contentType ?: "application/octet-stream",
                uploadedBy = uploadedBy,
                checksum = checksum
            )

            return fileRepository.save(fileEntity)
        } catch (e: Exception) {
            throw FileUploadException("Failed to upload file: ${e.message}")
        }
    }

    private fun calculateChecksum(file: MultipartFile): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = digest.digest(file.bytes)
        return bytes.joinToString("") { "%02x".format(it) }
    }

    @Transactional(readOnly = true)
    fun getFileById(id: Long): FileEntity {
        return fileRepository.findById(id)
            .orElseThrow { EntityNotFoundException("File not found with id: $id") }
    }

    @Transactional(readOnly = true)
    fun getUserFiles(uploadedBy: String): List<FileEntity> {
        return fileRepository.findAllByUploadedBy(uploadedBy)
    }

    @Transactional
    fun deleteFile(id: Long, uploadedBy: String) {
        val file = getFileById(id)
        if (file.uploadedBy != uploadedBy) {
            throw SecurityException("Not authorized to delete this file")
        }
        file.status = FileEntity.FileStatus.DELETED
        fileRepository.save(file)
    }
}