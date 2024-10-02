package com.krstudy.kapi.global.Security

import com.krstudy.kapi.domain.member.service.AuthTokenService
import com.krstudy.kapi.domain.member.service.MemberService
import com.krstudy.kapi.global.https.ReqData
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
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

    private val log: Logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler::class.java)

    @Value("\${frontend.url}")  // 프로퍼티 주입
    private var frontendUrl: String = "" // 기본값 설정

    @Throws(ServletException::class, IOException::class)
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        // 기본값을 빈 문자열로 설정하여 null 방지
        val redirectUrlAfterSocialLogin = rq.getCookieValue("redirectUrlAfterSocialLogin", "") ?: ""

        // rq.getMember()가 null인 경우에 대한 처리
        val member = rq.getMember() ?: run {
            log.error("Member not found during authentication success")
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Member not found")
            return
        }

        // redirectUrlAfterSocialLogin가 빈 문자열이 아닐 경우에만 isFrontUrl 호출
        if (redirectUrlAfterSocialLogin.isNotEmpty() && isFrontUrl(redirectUrlAfterSocialLogin)) {
            // 액세스 및 리프레시 토큰 생성
            val accessToken = authTokenService.genAccessToken(member)
            val refreshToken: String = member.jwtToken ?: "default_refresh_token_value"

            rq.destroySession()

            // 쿠키 설정
            rq.setCrossDomainCookie("accessToken", accessToken)
            rq.setCrossDomainCookie("refreshToken", refreshToken)
            rq.removeCookie("redirectUrlAfterSocialLogin")

            response.sendRedirect(redirectUrlAfterSocialLogin)
            return
        }

        super.onAuthenticationSuccess(request, response, authentication)
    }

    fun isFrontUrl(url: String?): Boolean {
        // url이 null이 아닌지 확인 후, 원하는 URL 패턴에 따라 조건을 설정
        return url != null && (url.startsWith(frontendUrl) || url.startsWith("http://localhost:8090"))
    }
}