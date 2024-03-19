package com.surl.studyurl.global.httpsdata;

import com.surl.studyurl.domain.member.entity.Member;
import com.surl.studyurl.domain.member.service.MemberService;
import com.surl.studyurl.global.app.AppConfig;
import com.surl.studyurl.global.security.SecurityUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Optional;

@Component
@RequestScope
@RequiredArgsConstructor
public class ReqData {
    private final MemberService memberService;
    private final HttpServletRequest req;
    private final HttpServletResponse resp;

    //캐시데이터
    private SecurityUser securityUser;
    private Member member;
    private Boolean isLogin;

    public Member getMember(){
        if(isLogin()) return null;
        if(member == null) member = memberService.getRefenceByNo(getSecurityUser().getId());
        return member;
    }

    private SecurityUser getSecurityUser(){
        if(securityUser!=null) return securityUser;
        securityUser = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(authentication -> authentication.getPrincipal() instanceof  SecurityUser)
                .map(authentication -> (SecurityUser) authentication.getPrincipal())
                .orElse(null);
        isLogin = securityUser!= null;
        return securityUser;
    }

    public boolean isLogout(){
        return !isLogin();
    }

    public boolean isLogin(){
        if(isLogin==null)getSecurityUser();
        return isLogin;
    }

    public String getCurrentUrlPath() {
        return req.getRequestURI();
    }

    public void setStatusCode(int statusCode) {
        resp.setStatus(statusCode);
    }

    public void setCrossDomainCookie(String name, String value) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path("/")
                .domain(getSiteCookieDomain())
                .sameSite("Strict")
                .secure(true)
                .httpOnly(true)
                .build();

        resp.addHeader("Set-Cookie", cookie.toString());
    }

    private String getSiteCookieDomain() {
        String cookieDomain = AppConfig.getSiteCookieDomain();

        if (!cookieDomain.equals("localhost")) {
            return cookieDomain = "." + cookieDomain;
        }

        return null;
    }

}
