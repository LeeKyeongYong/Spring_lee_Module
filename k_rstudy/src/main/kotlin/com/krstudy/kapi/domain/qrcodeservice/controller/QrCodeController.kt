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
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayOutputStream
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.core.io.ByteArrayResource

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

    @PostMapping("/extract")
    fun extractQRCodeInfo(@RequestParam("file") file: MultipartFile): String {
        val info = qrCodeService.extractInfoFromQRCode(file.bytes)
        return info ?: "QR Code information could not be extracted."
    }

    @PostMapping("/validate")
    fun validatePhoneNumber(@RequestParam("file") file: MultipartFile): String {
        val info = qrCodeService.extractInfoFromQRCode(file.bytes)
        return qrCodeService.validatePhoneNumber(info ?: "")
    }

    @GetMapping("/generate")
    fun generateQRCode(@RequestParam("text") text: String): ResponseEntity<Resource> {
        val qrCodeWriter = QRCodeWriter()
        val bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200)

        val outputStream = ByteArrayOutputStream()
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream)
        val imageBytes = outputStream.toByteArray()

        val resource = ByteArrayResource(imageBytes)
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_TYPE, "image/png")
            .body(resource)
    }

}