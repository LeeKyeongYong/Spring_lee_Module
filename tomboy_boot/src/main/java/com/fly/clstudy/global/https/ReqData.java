package com.fly.clstudy.global.https;

import com.fly.clstudy.domain.member.entity.Member;
import com.fly.clstudy.domain.member.service.MemberService;
import com.fly.clstudy.global.exceptions.GlobalException;
import com.fly.clstudy.global.jpa.util.UtStr;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import jakarta.servlet.http.Cookie;
import java.util.Arrays;

@Component
@RequestScope
@RequiredArgsConstructor
public class ReqData {
    private final MemberService memberService;
    private final HttpServletRequest req;
    private final HttpServletResponse resp;
    private Member member;

    public Member getMember() {

        if(member!=null) return member;

        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        member = memberService.findByUsername(name).get();
        return member;
    }

    public String getCurrentUrlPath() {
        return req.getRequestURI();
    }

    public void setStatusCode(int statusCode) {
        resp.setStatus(statusCode);
    }

    public void removeCookie(String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        resp.addCookie(cookie);
    }

    // 쿠키관련 시작
    public String getCookieValue(String cookieName, String defaultValue) {

        if (req.getCookies() == null) return defaultValue;

        return Arrays.stream(req.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(defaultValue);
    }

    public void setCookie(String actorUsername, String username) {
        Cookie cookie = new Cookie(actorUsername, username);
        cookie.setMaxAge(60 * 60 * 24 * 365);
        cookie.setPath("/");
        resp.addCookie(cookie);
    }
    // 쿠키관련 끝

}
