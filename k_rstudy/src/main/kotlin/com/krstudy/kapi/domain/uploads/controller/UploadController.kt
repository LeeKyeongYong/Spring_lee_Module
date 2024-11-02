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

@Controller
@RequestMapping("/upload")
class UploadController(
    @Value("\${file.upload-dir.windows}") private val uploadDir: String,
    private val fileUploadController: FileUploadController
) {
    @GetMapping
    fun showUploadForm(): String {
        return "domain/upload/upload"  // 업로드 폼 뷰 리턴
    }

    @PostMapping
    fun handleFileUpload(@RequestParam("file") files: Array<MultipartFile>, redirectAttributes: RedirectAttributes): String {
        // 파일 업로드를 FileUploadController로 위임
        val response = fileUploadController.uploadFile(files)

        // 성공적으로 업로드되었는지 확인 후 리다이렉트
        if (response.statusCode.is2xxSuccessful) {
            redirectAttributes.addFlashAttribute("message", "Files uploaded successfully")
        } else {
            redirectAttributes.addFlashAttribute("message", "Upload failed: ${response.body?.message}")
        }

        return "redirect:/upload"
    }
}