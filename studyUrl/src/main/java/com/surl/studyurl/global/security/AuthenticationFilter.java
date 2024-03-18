package com.surl.studyurl.global.security;

import com.surl.studyurl.standard.util.UtStr;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!request.getRequestURI().startsWith("/api/")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (List.of("/api/v1/members/login", "/api/v1/members/join", "/api/v1/members/logout").contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorization = request.getHeader("Authorization");
        authorization = authorization.substring("Bearer ".length());
        //System.out.println("authorization "+authorization);//json이 되었는가? Base64인코딩 된 인증정보
        String jsonStr = UtStr.base64Decode(authorization);
        //System.out.println("jsonStr "+jsonStr); //문자열로 디코딩이되었는가? 사용된 인증정보

        Map map = UtStr.json.toObj(jsonStr, Map.class);
        //System.out.println("map: "+map);//HTTP 요청 헤더의 Authorization 값으로 추가된 인증정보

        long securityUserId = (long) ((int) map.get("id"));
        String securityUserUsername = (String) map.get("username");
        List<String> securityUserAuthorities = (List<String>) map.get("authorities");

        SecurityUser securityUser = new SecurityUser(
                securityUserId,
                securityUserUsername,
                "",
                securityUserAuthorities
                        .stream()
                        .map(authority -> new SimpleGrantedAuthority(authority))
                        .toList()
        );

        SecurityContextHolder.getContext().setAuthentication(securityUser.genAuthentication());

        filterChain.doFilter(request, response);
    }
}
