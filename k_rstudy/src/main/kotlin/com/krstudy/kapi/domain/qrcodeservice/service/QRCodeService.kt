package com.krstudy.kapi.domain.qrcodeservice.service

import com.google.zxing.MultiFormatReader
import com.google.zxing.client.j2se.BufferedImageLuminanceSource
import com.krstudy.kapi.standard.base.QRCodeGenerator
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO
import com.google.zxing.Result
import com.google.zxing.common.HybridBinarizer


@Service
class QRCodeService (private val qrCodeGenerator: QRCodeGenerator) {

    fun createQRCode(url: String, width: Int, height: Int): ByteArray { //이미지 저장하기
        return qrCodeGenerator.generateQRCode(url, width, height)
    }

    fun extractInfoFromQRCode(imageBytes: ByteArray): String? {
        val image = ImageIO.read(ByteArrayInputStream(imageBytes))
        val source = BufferedImageLuminanceSource(image)
        val binaryBitmap = com.google.zxing.BinaryBitmap(HybridBinarizer(source))
        val reader = MultiFormatReader()

        return try {
            val result: Result = reader.decode(binaryBitmap)
            result.text
        } catch (e: Exception) {
            e.printStackTrace()  // 예외 로그를 찍어주는 것이 좋다.
            null
        }
    }

    fun validatePhoneNumber(info: String): String { //전화번호 비교
        val phoneNumberRegex = """(\d{3}-\d{4}-\d{4}|\d{10})""".toRegex()
        val phoneNumber = "010-1234-5678" // 체크할 번호
        return if (phoneNumberRegex.containsMatchIn(info)) {
            val matches = phoneNumberRegex.find(info)?.value
            if (matches == phoneNumber || matches?.replace("-", "") == phoneNumber.replace("-", "")) {
                "일치"
            } else {
                "비일치"
            }
        } else {
            "비일치"
        }
    }
}