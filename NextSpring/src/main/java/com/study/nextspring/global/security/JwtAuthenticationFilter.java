package com.study.nextspring.global.security;

import com.study.nextspring.domain.member.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.*;
import lombok.*;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.study.nextspring.global.httpsdata.*;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final ReqData rq;
    private final MemberService memberService;

    @Override
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        String accessToken_ = rq.getCookieValue("accessToken", "");
        System.out.println("accessToken from cookie: " + accessToken_);

        if (!request.getRequestURI().startsWith("/api/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 로그인, 회원가입, 로그아웃 API는 제외
        if (List.of("/api/v1/members/login", "/api/v1/members/join", "/api/v1/members/logout").contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        // Authorization 헤더에서 Bearer 토큰 처리
        String bearerToken = rq.getHeader("Authorization", null);
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            // 헤더에 토큰이 없으면 쿠키에서 가져오도록 처리
            accessToken_ = rq.getCookieValue("accessToken", "");
            if (accessToken_.isBlank()) {
                filterChain.doFilter(request, response);
                return;  // accessToken이 없으면 필터를 계속해서 실행하지 않음
            }
        } else {
            // Bearer 토큰이 있는 경우
            String tokensStr = bearerToken.substring("Bearer ".length());
            String[] tokens = tokensStr.split(" ", 2);
            String refreshToken = tokens[0];
            String accessToken = tokens.length == 2 ? tokens[1] : "";

            if (!accessToken.isBlank()) {
                // accessToken 유효성 체크
                if (!memberService.validateToken(accessToken)) {
                    System.out.println("Access token expired, refreshing...");
                    RespData<String> rs = memberService.refreshAccessToken(refreshToken);
                    accessToken = rs.getData();
                    rq.setHeader("Authorization", "Bearer " + refreshToken + " " + accessToken);
                }

                SecurityUser securityUser = memberService.getUserFromAccessToken(accessToken);
                System.out.println("Logged in user: " + securityUser.getUsername());
                rq.setLogin(securityUser);
            }
        }

        filterChain.doFilter(request, response);
    }

}