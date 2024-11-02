package com.krstudy.kapi.domain.uploads.controller

import com.krstudy.kapi.global.lgexecution.LogExecutionTime
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile

@Slf4j
@Controller
@RequestMapping("/upload")
class UploadController {

    @GetMapping
    @LogExecutionTime
    fun showUploadForm(): String {
        return "domain/upload/upload"
    }

    @PostMapping
    fun handleFileUpload(@RequestParam("file") files: Array<MultipartFile>): String {
        // 파일 처리 로직
        return "redirect:/upload"  // 업로드 후 리다이렉트
    }
}