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
        try {
            val member = memberService.getMemberByAuthentication(authentication) ?: run {
                response.sendRedirect("/member/login?error=memberNotFound")
                return
            }

            val token = authTokenService.genAccessToken(member)
            response.addHeader("Authorization", "Bearer $token")

            val redirectUrl = rq.getCookieValue("redirectUrlAfterSocialLogin", "/")
            response.sendRedirect(redirectUrl)
        } catch (e: Exception) {
            // 상세한 에러 로깅
            e.printStackTrace()
            logger.error("Authentication failed: ${e.message}", e)
            response.sendRedirect("/member/login?error=authenticationFailed")
        }
    }
}