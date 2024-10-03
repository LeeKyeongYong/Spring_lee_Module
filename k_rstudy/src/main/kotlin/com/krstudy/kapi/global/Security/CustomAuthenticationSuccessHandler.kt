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

        if (rq.isFrontUrl(redirectUrlAfterSocialLogin)) {
            val accessToken = authTokenService.genAccessToken(member)
            val refreshToken = member.jwtToken ?: run {
                val newRefreshToken = authTokenService.genRefreshToken(member.userid)
                memberService.updateMemberJwtToken(member.id, newRefreshToken)
                newRefreshToken
            }

            rq.destroySession()
            rq.setCrossDomainCookie("accessToken", accessToken)
            rq.setCrossDomainCookie("refreshToken", refreshToken)
            rq.removeCookie("redirectUrlAfterSocialLogin")

            response.sendRedirect(redirectUrlAfterSocialLogin)
            return
        }

        super.onAuthenticationSuccess(request, response, authentication)
    }
}