package com.krstudy.kapi.domain.pdfview.service

import com.krstudy.kapi.domain.member.repository.MemberRepository
import com.krstudy.kapi.domain.uploads.entity.FileEntity
import com.krstudy.kapi.domain.uploads.repository.UploadFileRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.slf4j.LoggerFactory // LoggerFactory 사용을 위해 필요
import org.springframework.web.multipart.MultipartFile // MultipartFile 사용을 위해 필요
import jakarta.persistence.EntityNotFoundException // EntityNotFoundException 사용을 위해 필요
import org.apache.pdfbox.pdmodel.PDDocument // PDDocument 사용을 위해 필요
import java.nio.file.Path // Path 사용을 위해 필요
import java.nio.file.Files // Files 사용을 위해 필요
import java.nio.file.StandardCopyOption // StandardCopyOption 사용을 위해 필요
import java.util.UUID // UUID 사용을 위해 필요
import javax.imageio.ImageIO // ImageIO 사용을 위해 필요
import org.springframework.core.io.Resource // Resource 사용을 위해 필요
import org.springframework.core.io.UrlResource // UrlResource 사용을 위해 필요
import java.security.MessageDigest // MessageDigest 사용을 위해 필요

// 프로젝트 내에서 정의한 커스텀 클래스나 Enum
import org.apache.pdfbox.rendering.PDFRenderer // PDFRenderer 사용을 위해 필요
import com.krstudy.kapi.domain.uploads.dto.FileStatusEnum
import com.krstudy.kapi.domain.uploads.exception.FileUploadException


@Service
class PdfService(
    private val fileRepository: UploadFileRepository,
    private val memberRepository: MemberRepository,
    @Value("\${file.upload-dir.windows}") private val uploadDir: String
) {
    private val logger = LoggerFactory.getLogger(PdfService::class.java)

    @Transactional
    fun uploadPdf(file: MultipartFile, username: String, title: String): FileEntity {
        logger.info("Starting PDF upload process for user: $username")

        val member = memberRepository.findByUsername(username)
            ?: throw EntityNotFoundException("사용자를 찾을 수 없습니다: $username")

        val uploadPath = Path.of(uploadDir).also { path ->
            if (!Files.exists(path)) {
                logger.info("Creating upload directory: $uploadDir")
                Files.createDirectories(path)
            }
        }

        return try {
            PDDocument.load(file.inputStream).use { document ->
                val storedFileName = "${UUID.randomUUID()}.pdf"
                val filePath = uploadPath.resolve(storedFileName)

                Files.copy(
                    file.inputStream,
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
                )

                createPdfPreviews(document, storedFileName)

                FileEntity(
                    originalFileName = file.originalFilename ?: "unknown.pdf",
                    storedFileName = storedFileName,
                    filePath = filePath.toString(),
                    fileSize = file.size,
                    fileType = "application/pdf",
                    contentType = file.contentType ?: "application/pdf",
                    checksum = calculateChecksum(file),
                    member = member,
                    status = FileStatusEnum.ACTIVE
                ).also {
                    fileRepository.save(it)
                    logger.info("File entity saved successfully: ${it.id}")
                }
            }
        } catch (e: Exception) {
            logger.error("Error during file upload", e)
            throw FileUploadException("파일 업로드 중 오류가 발생했습니다: ${e.message}")
        }
    }
    private fun createPdfPreviews(document: PDDocument, pdfFileName: String) {
        val renderer = PDFRenderer(document)
        val previewDir = Path.of(uploadDir, "previews")
        Files.createDirectories(previewDir)

        val pageCount = Math.min(document.numberOfPages, 10)
        for (i in 0 until pageCount) {
            val image = renderer.renderImageWithDPI(i, 150f)
            val previewPath = previewDir.resolve("${pdfFileName}_page_${i + 1}.png")
            ImageIO.write(image, "PNG", previewPath.toFile())
        }
    }

    fun getPdfPreview(fileId: Long, pageNum: Int): Resource {
        val file = fileRepository.findById(fileId)
            .orElseThrow { EntityNotFoundException("PDF not found") }

        val previewPath = Path.of(uploadDir, "previews", "${file.storedFileName}_page_$pageNum.png")
        return UrlResource(previewPath.toUri())
    }

    fun downloadPdf(fileId: Long): Resource {
        val file = fileRepository.findById(fileId)
            .orElseThrow { EntityNotFoundException("PDF not found") }

        return UrlResource(Path.of(file.filePath).toUri())
    }

    private fun calculateChecksum(file: MultipartFile): String {
        val digest = MessageDigest.getInstance("SHA-256")
        return digest.digest(file.bytes)
            .joinToString("") { "%02x".format(it) }
    }
}