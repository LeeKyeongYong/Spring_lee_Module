package com.krstudy.kapi.domain.uploads.controller


import com.krstudy.kapi.domain.uploads.service.FileServiceImpl
import com.krstudy.kapi.global.lgexecution.LogExecutionTime
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.ui.Model
import java.security.Principal

@Controller
@RequestMapping("/upload")
class UploadController(
    @Value("\${file.upload-dir.windows}") private val uploadDir: String,
    @Qualifier("uploadFileService") private val fileService: FileServiceImpl  // FileService 직접 주입
) {
    @GetMapping
    fun showUploadForm(model: Model): String {
        return "domain/upload/upload"
    }

    @PostMapping
    fun handleFileUpload(
        @RequestParam("file") files: Array<MultipartFile>,
        redirectAttributes: RedirectAttributes,
        principal: Principal
    ): String {
        try {
            val userId = principal.name
            val uploadedFiles = fileService.uploadFiles(files, userId)  // FileService 직접 호출
            redirectAttributes.addFlashAttribute("message", "파일이 성공적으로 업로드되었습니다.")
            redirectAttributes.addFlashAttribute("success", true)
        } catch (e: Exception) {
            redirectAttributes.addFlashAttribute("message", "업로드 실패: ${e.message}")
            redirectAttributes.addFlashAttribute("success", false)
        }
        return "redirect:/upload"
    }
}