package com.study.nextspring.global.security;

import com.study.nextspring.domain.member.entity.Member;
import com.study.nextspring.domain.member.service.MemberService;
import com.study.nextspring.global.httpsdata.ReqData;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends OncePerRequestFilter {
    private final MemberService memberService;
    private final ReqData rq;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!request.getRequestURI().startsWith("/api/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.substring("Bearer ".length());
        String[] tokenBits = token.split(" ", 2);

        if (tokenBits.length != 2) {
            filterChain.doFilter(request, response);
            return;
        }

        String apiKey = tokenBits[0];
        String accessToken = tokenBits[1];
        Member member = memberService.getMemberFromAccessToken(accessToken);

        if (member == null) {
            Optional<Member> opMemberByApiKey = memberService.findByApiKey(apiKey);

            if (opMemberByApiKey.isEmpty()) {
                filterChain.doFilter(request, response);
                return;
            }

            member = opMemberByApiKey.get();

            String newAccessToken = memberService.genAccessToken(member);

            response.setHeader("Authorization", "Bearer " + apiKey + " " + newAccessToken);
        }

        rq.setLogin(member);

        filterChain.doFilter(request, response);
    }
}