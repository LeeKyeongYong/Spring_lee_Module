package com.krstudy.kapi.domain.uploads.controller


import com.krstudy.kapi.domain.uploads.dto.FileStatusEnum
import com.krstudy.kapi.domain.uploads.service.FileServiceImpl
import jakarta.servlet.http.HttpServletResponse
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

@Controller
@RequestMapping("/api/pdf")
class PdfViewController(
    private val fileService: FileServiceImpl
) {
    private val logger = LoggerFactory.getLogger(PdfViewController::class.java)

    @PostMapping("/upload")
    @ResponseBody
    fun uploadPdf(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("title") title: String,
        @RequestParam("userId") userId: String
    ): ResponseEntity<Map<String, Any>> {
        return try {
            if (!file.contentType.equals("application/pdf")) {
                return ResponseEntity.badRequest()
                    .body(mapOf("resultCode" to "400", "message" to "PDF 파일만 업로드 가능합니다."))
            }

            val savedFile = fileService.uploadFiles(arrayOf(file), userId).firstOrNull()
                ?: throw RuntimeException("파일 업로드 실패")

            ResponseEntity.ok(mapOf(
                "resultCode" to "200",
                "message" to "파일이 성공적으로 업로드되었습니다.",
                "fileId" to savedFile.id
            ))
        } catch (e: Exception) {
            logger.error("PDF 업로드 실패", e)
            ResponseEntity.badRequest()
                .body(mapOf("resultCode" to "500", "message" to "파일 업로드 중 오류가 발생했습니다."))
        }
    }

    @GetMapping("/view")
    fun viewPdf(
        @RequestParam no: Long,
        @RequestParam(defaultValue = "1") p: Int,
        @RequestParam(defaultValue = "1") page: Int,
        model: Model
    ): String {
        val file = fileService.getFileById(no)
        val pdfFile = File(file.filePath)

        PDDocument.load(pdfFile).use { document ->
            val pageCount = document.numberOfPages
            val lastPage = minOf(pageCount, 10) // 최대 10페이지까지만 미리보기 제공

            model.addAttribute("book", file)
            model.addAttribute("p", p)
            model.addAttribute("last", lastPage)
            model.addAttribute("page", page)
        }

        return "documents/view"
    }

    @GetMapping("/preview/{fileId}/{page}")
    fun getPreviewImage(
        @PathVariable fileId: Long,
        @PathVariable page: Int,
        response: HttpServletResponse
    ) {
        val file = fileService.getFileById(fileId)
        val pdfFile = File(file.filePath)

        try {
            PDDocument.load(pdfFile).use { document ->
                val renderer = PDFRenderer(document)
                val image: BufferedImage = renderer.renderImageWithDPI(page - 1, 150f)
                response.contentType = "image/png"
                ImageIO.write(image, "png", response.outputStream)
            }
        } catch (e: IOException) {
            logger.error("PDF 페이지 렌더링 실패", e)
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
        }
    }
}