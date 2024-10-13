package com.krstudy.kapi.domain.emails.service


import com.krstudy.kapi.domain.emails.entity.VerificationCode
import com.krstudy.kapi.emails.repository.VerificationCodeRepository
import java.time.LocalDateTime
import java.util.UUID
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import com.krstudy.kapi.global.exception.GlobalException
import com.krstudy.kapi.global.exception.MessageCode
import  com.krstudy.kapi.domain.emails.entity.Email
import com.krstudy.kapi.domain.emails.repository.EmailRepository
import com.krstudy.kapi.domain.files.repository.FileRepository
import com.krstudy.kapi.domain.files.service.FileService
import jakarta.mail.internet.MimeMessage
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.web.multipart.MultipartFile

@Service
class EmailService(
    private val mailSender: JavaMailSender,
    private val verificationCodeRepository: VerificationCodeRepository,
    private val emailRepository: EmailRepository,
    private val fileRepository: FileRepository
) {
    private val EXPIRATION_TIME_IN_MINUTES = 5

    // 메일 발송 메소드
    fun sendSimpleVerificationMail(email: Email, file: MultipartFile?) {
        val verificationCode = generateVerificationCode() // 자동으로 createAt 설정됨

        // MimeMessage 사용하여 파일 첨부
        val mimeMessage: MimeMessage = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(mimeMessage, true) // true로 설정하여 파일 첨부 가능

        helper.setFrom(email.finalEmail) // 보내는사람
        helper.setTo(email.receiverEmail) // 받는사람
        helper.setSubject("Email Verification For ${email.receiverEmail}")
        helper.setText(verificationCode.generateCodeMessage())

        // 파일이 존재하는 경우 첨부
        file?.let {
            if (!it.isEmpty) {
                helper.addAttachment(it.originalFilename ?: "attachment", it)
                // 파일도 저장
                val fileService = FileService(fileRepository) // 의존성 주입 방식에 따라 조정
                fileService.saveFile(it, email.id, "EMAIL") // 이메일 ID와 함께 파일 저장
            }
        }

        mailSender.send(mimeMessage)
        verificationCodeRepository.save(verificationCode) // DB에 저장

        // EmailLog 저장
        val emailLog = Email(
            serviceEmail = email.serviceEmail,
            title = "Email Verification For ${email.receiverEmail}",
            content = verificationCode.generateCodeMessage(),
            receiverEmail = email.receiverEmail
        )
        emailRepository.save(emailLog) // 이메일 발송 내용 저장
    }

    // 인증 코드 검증 메소드
    fun verifyCode(code: String, verifiedAt: LocalDateTime) {
        val verificationCode = verificationCodeRepository.findByCode(code)
            ?: throw GlobalException(MessageCode.NOT_FOUND_RESOURCE)

        if (verificationCode.isExpired(verifiedAt)) {
            throw GlobalException(MessageCode.EXPIRED_VERIFICATION_CODE) // 적절한 예외 코드로 변경
        }

        verificationCodeRepository.delete(verificationCode) // DB에서 삭제
    }

    // 인증 코드 생성 메소드
    private fun generateVerificationCode(): VerificationCode {
        val code = UUID.randomUUID().toString()
        return VerificationCode(
            code = code,
            expirationTimeInMinutes = EXPIRATION_TIME_IN_MINUTES
        ) // createAt은 자동으로 설정됨
    }
}
