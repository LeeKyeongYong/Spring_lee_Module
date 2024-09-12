package com.krstudy.kapi.domain.qrcodeservice.service

import com.krstudy.kapi.standard.base.QRCodeGenerator
import org.springframework.stereotype.Service

@Service
class QRCodeService (private val qrCodeGenerator: QRCodeGenerator) {

    fun createQRCode(url: String, width: Int, height: Int): ByteArray {
        return qrCodeGenerator.generateQRCode(url, width, height)
    }
}