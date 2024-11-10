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
    fun uploadPdf(file: MultipartFile, userId: String, title: String): FileEntity {
        if (!file.contentType.equals("application/pdf")) {
            throw IllegalArgumentException("PDF files only are allowed")
        }

        val member = memberRepository.findByUsername(userId)
            ?: throw EntityNotFoundException("Member not found with ID: $userId")

        return try {
            // PDF 문서 로드 및 페이지 수 확인
            val document = PDDocument.load(file.inputStream)
            val pageCount = document.numberOfPages

            // 파일 저장을 위한 UUID 생성
            val storedFileName = "${UUID.randomUUID()}.pdf"
            val filePath = Path.of(uploadDir).resolve(storedFileName)

            // 디렉토리 생성
            Files.createDirectories(filePath.parent)

            // PDF 파일 저장
            Files.copy(
                file.inputStream,
                filePath,
                StandardCopyOption.REPLACE_EXISTING
            )

            // 미리보기 이미지 생성
            createPdfPreviews(document, storedFileName)

            // FileEntity 생성 및 저장
            val fileEntity = FileEntity(
                originalFileName = file.originalFilename ?: "unknown.pdf",
                storedFileName = storedFileName,
                filePath = filePath.toString(),
                fileSize = file.size,
                fileType = "application/pdf",
                contentType = "application/pdf",
                checksum = calculateChecksum(file),
                member = member,
                status = FileStatusEnum.ACTIVE
            )

            document.close()
            fileRepository.save(fileEntity)
        } catch (e: Exception) {
            logger.error("Failed to upload PDF: ${e.message}", e)
            throw FileUploadException("Failed to upload PDF: ${e.message}")
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