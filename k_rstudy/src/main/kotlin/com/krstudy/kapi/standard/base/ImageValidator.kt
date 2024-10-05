package com.krstudy.kapi.standard.base


import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.security.MessageDigest
import javax.imageio.ImageIO

class ImageValidator : ConstraintValidator<ValidImage, MultipartFile?> {
    private var maxSize: Long = 0
    private lateinit var types: Array<String>

    override fun initialize(constraintAnnotation: ValidImage) {
        maxSize = constraintAnnotation.maxSize
        types = constraintAnnotation.types
    }

    override fun isValid(file: MultipartFile?, context: ConstraintValidatorContext): Boolean {
        // null 체크: 필수가 아닌 경우 null을 허용
        file ?: return true

        // 기본 검증
        if (!validateBasics(file, context)) return false

        // 파일 내용 검증
        return validateContent(file, context)
    }

    private fun validateBasics(file: MultipartFile, context: ConstraintValidatorContext): Boolean {
        // 빈 파일 체크
        if (file.isEmpty) {
            addConstraintViolation(context, "파일이 비어 있습니다")
            return false
        }

        // 파일 크기 체크
        if (file.size > maxSize) {
            addConstraintViolation(context, "파일 크기는 ${maxSize / 1024 / 1024}MB를 초과할 수 없습니다")
            return false
        }

        // MIME 타입 체크
        if (file.contentType !in types) {
            addConstraintViolation(context, "허용되지 않는 파일 형식입니다. 허용된 형식: ${types.joinToString(", ")}")
            return false
        }

        return true
    }

    private fun validateContent(file: MultipartFile, context: ConstraintValidatorContext): Boolean {
        try {
            val inputStream = file.inputStream
            val imageBytes = inputStream.readBytes()

            // 매직 넘버 확인
            if (!validateMagicNumber(imageBytes)) {
                addConstraintViolation(context, "유효하지 않은 이미지 파일입니다")
                return false
            }

            // 이미지 메타데이터 분석
            if (!validateImageMetadata(imageBytes)) {
                addConstraintViolation(context, "이미지 메타데이터가 유효하지 않습니다")
                return false
            }

            // 파일 해시 계산 및 검증 (옵션)
            val fileHash = calculateFileHash(imageBytes)
            // TODO: 해시값을 데이터베이스나 허용 목록과 비교

            return true
        } catch (e: IOException) {
            addConstraintViolation(context, "이미지 파일 검증 중 오류 발생: ${e.message}")
            return false
        }
    }

    private fun validateMagicNumber(bytes: ByteArray): Boolean {
        // JPEG, PNG, GIF 매직 넘버 확인
        return when {
            bytes.size > 2 && bytes[0] == 0xFF.toByte() && bytes[1] == 0xD8.toByte() -> true // JPEG
            bytes.size > 8 && bytes.slice(0..7).toByteArray().contentEquals(byteArrayOf(0x89.toByte(), 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A)) -> true // PNG
            bytes.size > 3 && bytes.slice(0..2).toByteArray().contentEquals(byteArrayOf(0x47, 0x49, 0x46)) -> true // GIF
            else -> false
        }
    }

    private fun validateImageMetadata(bytes: ByteArray): Boolean {
        return try {
            val bis = bytes.inputStream().buffered()
            val image = ImageIO.read(bis)
            image != null && image.width > 0 && image.height > 0
        } catch (e: IOException) {
            false
        }
    }

    private fun calculateFileHash(bytes: ByteArray): String {
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    private fun addConstraintViolation(context: ConstraintValidatorContext, message: String) {
        context.disableDefaultConstraintViolation()
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation()
    }
}