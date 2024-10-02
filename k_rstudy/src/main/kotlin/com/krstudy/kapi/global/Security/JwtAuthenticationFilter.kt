package com.krstudy.kapi.global.Security

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

        val bearerToken = rq.getHeader("Authorization","") ?: return filterChain.doFilter(request, response)

        val tokensStr = bearerToken.substring("Bearer ".length).takeIf { it.isNotBlank() } ?: return
        val tokens = tokensStr.split(" ", limit = 2)
        val refreshToken: String = tokens[0]
        var accessToken: String = tokens.getOrElse(1) { "" }

        // 엑세스 토큰이 존재하면
        if (accessToken.isNotBlank()) {
            // 유효성 체크하여 만료되었으면 리프레시 토큰으로 새로운 엑세스 토큰을 발급받고 응답헤더에 추가
            if (!memberService.validateToken(accessToken)) {
                val rs: RespData<String> = memberService.refreshAccessToken(refreshToken)
                accessToken = rs.data ?: return filterChain.doFilter(request, response) // 데이터가 null일 경우 처리
                rq.setHeader("Authorization", "Bearer $refreshToken $accessToken")
            }

            val securityUser = memberService.getUserFromAccessToken(accessToken)
            rq.setLogin(securityUser)
        } else {
            // 토큰이 쿠키로 들어온 경우
            var accessToken = rq.getCookieValue("accessToken", "") ?: ""

            // 엑세스 토큰이 존재하면
            if (accessToken.isNotBlank()) {
                // 유효성 체크하여 만료되었으면 리프레시 토큰으로 새로운 엑세스 토큰을 발급받고 응답쿠키에 추가
                if (!memberService.validateToken(accessToken)) {
                    val refreshToken = rq.getCookieValue("refreshToken", "") ?: ""

                    val rs: RespData<String> = memberService.refreshAccessToken(refreshToken)
                    accessToken = rs.data ?: return filterChain.doFilter(request, response) // 데이터가 null일 경우 처리
                    rq.setCrossDomainCookie("accessToken", accessToken)
                }

                val securityUser = memberService.getUserFromAccessToken(accessToken)
                rq.setLogin(securityUser)
            }
        }

        filterChain.doFilter(request, response)
    }
}
