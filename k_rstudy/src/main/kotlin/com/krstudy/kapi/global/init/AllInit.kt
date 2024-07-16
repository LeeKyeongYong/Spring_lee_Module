package com.krstudy.kapi.com.krstudy.kapi.global.init

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
            if (memberService.findByUsername("system").isPresent) return@ApplicationRunner

            memberService.join("system", "1234")
            memberService.join("admin", "1234")
        }
    }
}