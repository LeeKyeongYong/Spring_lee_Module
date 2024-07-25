package com.krstudy.kapi.com.krstudy.kapi.global.init

import com.krstudy.kapi.com.krstudy.kapi.domain.member.service.MemberService
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order

@Configuration
class AllInit(private val memberService: MemberService) {

    private val log = LoggerFactory.getLogger(AllInit::class.java)

    @Bean
    @Order(2)
    fun initAll(): ApplicationRunner {
        return ApplicationRunner { args ->
           // if (memberService.findByUsername("system").isPresent) return@ApplicationRunner

            // findByUsername이 Member?를 반환한다고 가정
            if (memberService.findByUsername("system") != null) return@ApplicationRunner

            memberService.join("system", "1234","")
            memberService.join("admin", "1234","")
        }
    }
}