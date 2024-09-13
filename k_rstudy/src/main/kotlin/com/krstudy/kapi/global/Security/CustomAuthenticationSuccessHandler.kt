package com.krstudy.kapi.global.Security

import com.krstudy.kapi.domain.member.service.MemberService
import com.krstudy.kapi.global.exception.CustomException
import com.krstudy.kapi.global.exception.ErrorCode
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.userdetails.UsernameNotFoundException
import java.io.IOException
class CustomAuthenticationSuccessHandler (
    private val memberService: MemberService
): SavedRequestAwareAuthenticationSuccessHandler() {

    @Throws(ServletException::class, IOException::class)
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {


        val isAdmin = authentication.authorities.any {
            it.authority == "ROLE_ADMIN"
        }

        if (isAdmin) {
            setDefaultTargetUrl("/admin/**")
        } else {
            setDefaultTargetUrl("/")
        }
        super.onAuthenticationSuccess(request, response, authentication)
    }
}