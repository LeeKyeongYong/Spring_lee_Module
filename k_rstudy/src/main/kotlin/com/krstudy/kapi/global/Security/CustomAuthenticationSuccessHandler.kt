package com.krstudy.kapi.global.Security

import com.krstudy.kapi.domain.member.service.AuthTokenService
import com.krstudy.kapi.domain.member.service.MemberService
import com.krstudy.kapi.global.https.ReqData
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.io.IOException
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class CustomAuthenticationSuccessHandler(
    private val memberService: MemberService,
    private val rq: ReqData,
    private val authTokenService: AuthTokenService
) : SavedRequestAwareAuthenticationSuccessHandler() {

    private val log: Logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler::class.java)

    @Throws(ServletException::class, IOException::class)
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val redirectUrlAfterSocialLogin = rq.getCookieValue("redirectUrlAfterSocialLogin", "")

        // rq.getMember()가 null인 경우에 대한 처리
        val member = rq.getMember() ?: run {
            log.error("Member not found during authentication success")
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Member not found")
            return
        }

        if (rq.isFrontUrl(redirectUrlAfterSocialLogin)) {
            val accessToken = authTokenService.genAccessToken(member)

            // refreshToken도 nullable일 수 있으므로 처리
            val refreshToken = member.jwtToken ?: run {
                log.error("Refresh token not found for member: ${member.id}")
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Refresh token not found")
                return
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