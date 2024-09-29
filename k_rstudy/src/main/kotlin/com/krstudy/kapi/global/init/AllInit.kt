package com.krstudy.kapi.global.init

import com.krstudy.kapi.domain.member.service.MemberService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.slf4j.LoggerFactory

@Configuration
class AllInit(private val memberService: MemberService) {
    private val log = LoggerFactory.getLogger(AllInit::class.java)

    @Bean
    @Order(2)
    fun initAll(): ApplicationRunner {
        return ApplicationRunner {
            CoroutineScope(Dispatchers.Default).launch {
                if (memberService.findByUserid("system") == null) {
                    memberService.join("system", "시스템관리자", "system@example.com", "1234", null, null, "")
                    memberService.join("admin", "관리자", "admin@example.com", "1234", null, null, "")
                }
            }
        }
    }
}