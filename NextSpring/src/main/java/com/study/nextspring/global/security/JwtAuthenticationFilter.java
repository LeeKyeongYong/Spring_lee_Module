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
        System.out.println("accessToken = " + accessToken_);

        if (!request.getRequestURI().startsWith("/api/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 로그인, 회원가입, 로그아웃 API는 제외
        if (List.of("/api/v1/members/login", "/api/v1/members/join", "/api/v1/members/logout").contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        String bearerToken = rq.getHeader("Authorization", null);
        System.out.println("bearerToken from header: " + bearerToken);

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            // 토큰이 헤더로 들어온 경우
            String tokensStr = bearerToken.substring("Bearer ".length());
            String[] tokens = tokensStr.split(" ", 2);
            String refreshToken = tokens[0];
            String accessToken = tokens.length == 2 ? tokens[1] : "";

            System.out.println("accessToken: " + accessToken);

            // 엑세스 토큰이 존재하면
            if (!accessToken.isBlank()) {
                // 유효성 체크하여 만료되었으면 리프레시 토큰으로 새로운 엑세스 토큰을 발급받고 응답헤더에 추가
                if (!memberService.validateToken(accessToken)) {
                    System.out.println("Access token expired, refreshing...");
                    RespData<String> rs = memberService.refreshAccessToken(refreshToken);
                    accessToken = rs.getData();
                    rq.setHeader("Authorization", "Bearer " + refreshToken + " " + accessToken);
                    System.out.println("New accessToken: " + accessToken);
                }

                SecurityUser securityUser = memberService.getUserFromAccessToken(accessToken);
                System.out.println("Logged in user: " + securityUser.getUsername());
                // 세션에 로그인하는 것이 아닌 1회성(이번 요청/응답 생명주기에서만 인정됨)으로 로그인 처리
                // API 요청은, 로그인이 필요하다면 이렇게 매번 요청마다 로그인 처리가 되어야 하는게 맞다.
                rq.setLogin(securityUser);
            }
        } else {
            // 토큰이 쿠키로 들어온 경우
            String accessToken = rq.getCookieValue("accessToken", "");
            System.out.println("accessToken from cookie: " + accessToken);

            // 엑세스 토큰이 존재하면
            if (!accessToken.isBlank()) {
                // 유효성 체크하여 만료되었으면 리프레시 토큰으로 새로운 엑세스 토큰을 발급받고 응답쿠키에 추가
                if (!memberService.validateToken(accessToken)) {
                    String refreshToken = rq.getCookieValue("refreshToken", "");
                    RespData<String> rs = memberService.refreshAccessToken(refreshToken);
                    accessToken = rs.getData();
                    rq.setCrossDomainCookie("accessToken", accessToken);
                    System.out.println("New accessToken from cookie: " + accessToken);
                }


                SecurityUser securityUser = memberService.getUserFromAccessToken(accessToken);
                System.out.println("Logged in user: " + securityUser.getUsername());
                // 세션에 로그인하는 것이 아닌 1회성(이번 요청/응답 생명주기에서만 인정됨)으로 로그인 처리
                // API 요청은, 로그인이 필요하다면 이렇게 매번 요청마다 로그인 처리가 되어야 하는게 맞다.
                rq.setLogin(securityUser);
            }
        }

        filterChain.doFilter(request, response);
    }
}