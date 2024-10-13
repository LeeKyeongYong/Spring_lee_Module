package com.krstudy.kapi.global.Security.filter

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
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        if (!request.requestURI.startsWith("/api/")) {
            filterChain.doFilter(request, response)
            return
        }

        val bearerToken = rq.getHeader("Authorization", "")
        if (bearerToken.isNullOrBlank() || !bearerToken.startsWith("Bearer ")) {
            handleCookieBasedAuth(request, response, filterChain)
            return
        }

        val tokensStr = bearerToken.substring("Bearer ".length)
        val tokens = tokensStr.split(" ", limit = 2)
        if (tokens.size < 2) {
            filterChain.doFilter(request, response)
            return
        }

        val refreshToken = tokens[0]
        val accessToken = tokens[1]

        if (accessToken.isNotBlank()) {
            handleTokenAuth(refreshToken, accessToken, request, response, filterChain)
        } else {
            handleCookieBasedAuth(request, response, filterChain)
        }
    }

    private fun handleTokenAuth(refreshToken: String, accessToken: String, request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val validatedAccessToken = if (!memberService.validateToken(accessToken)) {
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
    }

    private fun handleCookieBasedAuth(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
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

            val rs: RespData<String> = memberService.refreshAccessToken(refreshToken)
            val newAccessToken = rs.data ?: run {
                filterChain.doFilter(request, response)
                return
            }
            rq.setCrossDomainCookie("accessToken", newAccessToken)
            val securityUser = memberService.getUserFromAccessToken(newAccessToken)
            rq.setLogin(securityUser)
        } else {
            val securityUser = memberService.getUserFromAccessToken(accessToken)
            rq.setLogin(securityUser)
        }

        filterChain.doFilter(request, response)
    }
}