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
        return ApplicationRunner { args ->
            CoroutineScope(Dispatchers.Default).launch {
                // findByUsername이 Member?를 반환한다고 가정
                if (memberService.findByUserid("system") != null) return@launch

                // 회원가입 시 역할이 자동으로 설정되도록 변경
                memberService.join("system","시스템관리자", "abc@lky.co.kr","1234", "") // roleType은 비워두면 기본값 ROLE_ADMIN이 설정됨
                memberService.join("admin", "관리자","abc@lky.co.kr","1234", "") // roleType은 비워두면 기본값 ROLE_ADMIN이 설정됨
            }
        }
    }
}