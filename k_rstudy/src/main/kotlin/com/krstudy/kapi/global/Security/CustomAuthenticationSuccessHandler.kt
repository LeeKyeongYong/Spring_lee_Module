package com.krstudy.kapi.global.Security

import com.krstudy.kapi.domain.member.service.MemberService
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.io.IOException
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class CustomAuthenticationSuccessHandler(
    private val memberService: MemberService
) : SavedRequestAwareAuthenticationSuccessHandler() {

    private val log: Logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler::class.java)

    @Throws(ServletException::class, IOException::class)
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val isAdmin = authentication.authorities.any { it.authority == "ADMIN" }

        if (isAdmin) {
            log.info("User is an admin: ${authentication.name}") // 관리자 로그
            setDefaultTargetUrl("/admin/**")
        } else {
            log.info("User is not an admin: ${authentication.name}") // 일반 사용자 로그
            setDefaultTargetUrl("/")
        }

        super.onAuthenticationSuccess(request, response, authentication)
    }
}
