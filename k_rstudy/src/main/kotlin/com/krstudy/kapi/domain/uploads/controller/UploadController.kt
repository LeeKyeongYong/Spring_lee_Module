package com.krstudy.kapi.domain.uploads.controller

import com.krstudy.kapi.global.lgexecution.LogExecutionTime
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.ui.Model

@Controller
@RequestMapping("/upload")
class UploadController(
    @Value("\${file.upload-dir.windows}") private val uploadDir: String,
    private val fileUploadController: FileUploadController
) {
    @GetMapping
    fun showUploadForm(model: Model): String {
        return "domain/upload/upload"
    }

    @PostMapping
    fun handleFileUpload(
        @RequestParam("file") files: Array<MultipartFile>,
        redirectAttributes: RedirectAttributes
    ): String {
        try {
            // FileUploadController를 통해 파일 업로드 처리
            fileUploadController.uploadFile(files)

            // 성공 메시지 설정
            redirectAttributes.addFlashAttribute("message", "파일이 성공적으로 업로드되었습니다.")
            redirectAttributes.addFlashAttribute("success", true)
        } catch (e: Exception) {
            // 실패 메시지 설정
            redirectAttributes.addFlashAttribute("message", "업로드 실패: ${e.message}")
            redirectAttributes.addFlashAttribute("success", false)
        }

        return "redirect:/upload"
    }
}