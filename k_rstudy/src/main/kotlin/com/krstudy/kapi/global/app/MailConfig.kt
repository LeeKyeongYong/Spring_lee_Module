package com.krstudy.kapi.global.app

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.util.Properties

@Configuration
class MailConfig {

    @Value("\${spring.mail.host}")
    private lateinit var mailServerHost: String

    @Value("\${spring.mail.port}")
    private lateinit var mailServerPort: String

    @Value("\${spring.mail.username}")
    private lateinit var mailServerUsername: String

    @Value("\${spring.mail.password}")
    private lateinit var mailServerPassword: String

    @Bean
    fun javaMailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = mailServerHost
        mailSender.port = mailServerPort.toInt() // 포트는 Int형으로 변환

        mailSender.username = mailServerUsername
        mailSender.password = mailServerPassword

        val properties: Properties = mailSender.javaMailProperties
        properties["mail.transport.protocol"] = "smtp"
        properties["mail.smtp.auth"] = "true"
        properties["mail.smtp.starttls.enable"] = "true"

        return mailSender
    }
}