package com.krstudy.kapi.domain.qrcodeservice.controller

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.krstudy.kapi.domain.qrcodeservice.service.QRCodeService
import lombok.extern.slf4j.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import java.io.ByteArrayOutputStream
@RestController
@RequestMapping("/v1/qrcode")
@Slf4j
class QRCodeController(private val qrCodeService: QRCodeService) {
    @GetMapping("/qr", produces = [MediaType.IMAGE_PNG_VALUE])
    fun createQRCode(): ResponseEntity<ByteArray> {
        val url = "https://velog.io/@sleekydevzero86/posts"
        val width = 200
        val height = 200

        val qrCodeImage = qrCodeService.createQRCode(url, width, height)

        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_PNG)
            .body(qrCodeImage)
    }
}