package com.krstudy.kapi.global.Security.filter

import org.slf4j.LoggerFactory
import com.krstudy.kapi.domain.member.service.MemberService
import com.krstudy.kapi.global.https.ReqData
import com.krstudy.kapi.global.https.RespData
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
@Component
@RequiredArgsConstructor
class JwtAuthenticationFilter(
    private val rq: ReqData,
    private val memberService: MemberService
) : OncePerRequestFilter() {

    @Throws(Exception::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // 1. API 경로가 아닌 경우 그대로 통과
        if (!request.requestURI.startsWith("/api/")) {
            filterChain.doFilter(request, response)
            return
        }

        try {
            // 2. Bearer 토큰 확인
            val bearerToken = rq.getHeader("Authorization", "")
            if (bearerToken.isBlank() || !bearerToken.startsWith("Bearer ")) {
                handleCookieBasedAuth(request, response, filterChain)
                return
            }

            // 3. 토큰 파싱
            val tokensStr = bearerToken.substring("Bearer ".length)
            val tokens = tokensStr.split(" ", limit = 2)
            if (tokens.size < 2) {
                handleCookieBasedAuth(request, response, filterChain)
                return
            }

            val refreshToken = tokens[0]
            val accessToken = tokens[1]

            // 4. 토큰 기반 인증 처리
            if (accessToken.isNotBlank()) {
                handleTokenAuth(refreshToken, accessToken, request, response, filterChain)
            } else {
                handleCookieBasedAuth(request, response, filterChain)
            }
        } catch (e: Exception) {
            // 5. 예외 발생 시 인증 없이 계속 진행
            logger.error("Authentication error", e)
            filterChain.doFilter(request, response)
        }
    }

    private fun handleTokenAuth(
        refreshToken: String,
        accessToken: String,
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val validatedAccessToken = if (!memberService.validateToken(accessToken)) {
                // 액세스 토큰이 유효하지 않은 경우
                val rs: RespData<String> = memberService.refreshAccessToken(refreshToken)
                rs.data?.also { newAccessToken ->
                    rq.setHeader("Authorization", "Bearer $refreshToken $newAccessToken")
                } ?: run {
                    filterChain.doFilter(request, response)
                    return
                }
            } else {
                accessToken
            }

            val securityUser = memberService.getUserFromAccessToken(validatedAccessToken)
            rq.setLogin(securityUser)
            filterChain.doFilter(request, response)
        } catch (e: Exception) {
            logger.error("Token authentication error", e)
            filterChain.doFilter(request, response)
        }
    }

    private fun handleCookieBasedAuth(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val accessToken = rq.getCookieValue("accessToken", "")
            if (accessToken.isNullOrBlank()) {
                filterChain.doFilter(request, response)
                return
            }

            if (!memberService.validateToken(accessToken)) {
                val refreshToken = rq.getCookieValue("refreshToken", "")
                if (refreshToken.isNullOrBlank()) {
                    filterChain.doFilter(request, response)
                    return
                }

                try {
                    val rs: RespData<String> = memberService.refreshAccessToken(refreshToken)
                    val newAccessToken = rs.data ?: run {
                        filterChain.doFilter(request, response)
                        return
                    }

                    rq.setCrossDomainCookie("accessToken", newAccessToken)
                    val securityUser = memberService.getUserFromAccessToken(newAccessToken)
                    rq.setLogin(securityUser)
                } catch (e: Exception) {
                    logger.error("Token refresh error", e)
                    filterChain.doFilter(request, response)
                    return
                }
            } else {
                val securityUser = memberService.getUserFromAccessToken(accessToken)
                rq.setLogin(securityUser)
            }

            filterChain.doFilter(request, response)
        } catch (e: Exception) {
            logger.error("Cookie authentication error", e)
            filterChain.doFilter(request, response)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)
    }
}