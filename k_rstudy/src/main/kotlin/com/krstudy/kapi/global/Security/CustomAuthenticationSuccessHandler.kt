package com.krstudy.kapi.global.Security

import com.krstudy.kapi.domain.member.service.AuthTokenService
import com.krstudy.kapi.domain.member.service.MemberService
import com.krstudy.kapi.global.https.ReqData
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.io.IOException
@Component
@Transactional(readOnly = true)
class CustomAuthenticationSuccessHandler(
    private val memberService: MemberService,
    private val rq: ReqData,
    private val authTokenService: AuthTokenService
) : SavedRequestAwareAuthenticationSuccessHandler() {

    @Throws(ServletException::class, IOException::class)
    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        val redirectUrlAfterSocialLogin = rq.getCookieValue("redirectUrlAfterSocialLogin", "") ?: ""

        val member = memberService.getMemberByAuthentication(authentication) ?: run {
            response.sendRedirect("/member/login?error=memberNotFound")
            return
        }

        // 필요에 따라 member의 정보에 따라 JWT 토큰을 생성, expireSeconds를 설정 (예: 3600초)
        val token = authTokenService.genToken(member, 3600)

        // 생성된 토큰을 응답에 추가하거나 리다이렉트할 URL에 포함
        response.addHeader("Authorization", "Bearer $token")
        super.onAuthenticationSuccess(request, response, authentication)
    }
}
