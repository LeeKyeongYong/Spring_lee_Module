package com.study.nextspring.global.httpsdata;

import com.study.nextspring.domain.member.entity.Member;
import com.study.nextspring.domain.member.service.MemberService;
import com.study.nextspring.global.app.AppConfig;
import com.study.nextspring.global.security.SecurityUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

@Component
@RequestScope
@RequiredArgsConstructor
public class ReqData {
    @Autowired private final HttpServletRequest req;
    @Autowired private final HttpServletResponse resp;
    @Autowired private final CookieService cookieService;
    @Autowired private final MemberService memberService;

    @PersistenceContext
    private EntityManager entityManager;

    private SecurityUser user;
    private Member member;
    private Boolean isLogin;
    private Boolean isAdmin;

    // HTTP 관련 메서드들
    public void setHeader(String name, String value) {
        resp.setHeader(name, value);
    }

    public String getHeader(String name) {
        return req.getHeader(name);
    }

    public void setStatusCode(int statusCode) {
        resp.setStatus(statusCode);
    }

    // URL 관련 메서드들
    public String getCurrentUrlPath() {
        return req.getRequestURI();
    }

    public boolean isApi() {
        String xRequestedWith = req.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(xRequestedWith);
    }


    public void setCookie(String name, String value) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path("/")
                .domain("localhost")
                .sameSite("Strict")
                .secure(true)
                .httpOnly(true)
                .build();
        resp.addHeader("Set-Cookie", cookie.toString());
    }

    private String getSiteCookieDomain() {
        String cookieDomain = AppConfig.getSiteCookieDomain();
        if (!cookieDomain.equals("localhost")) {
            return "." + cookieDomain;
        }
        return null;
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

    public void removeCrossDomainCookie(String name) {
        ResponseCookie cookie = ResponseCookie.from(name, "")
                .path("/")
                .domain(getSiteCookieDomain())
                .maxAge(0)
                .sameSite("Strict")
                .secure(true)
                .httpOnly(true)
                .build();

        resp.addHeader("Set-Cookie", cookie.toString());
    }

    public Cookie getCookie(String name) {
        return cookieService.getCookie(name);
    }

    public String getCookieValue(String name) {
        return Optional
                .ofNullable(req.getCookies())
                .stream() // 1 ~ 0
                .flatMap(cookies -> Arrays.stream(cookies))
                .filter(cookie -> cookie.getName().equals(name))
                .map(cookie -> cookie.getValue())
                .findFirst()
                .orElse(null);
    }

    public void removeCookie(String name) {
        Cookie cookie = getCookie(name);
        if (cookie == null) {
            return;
        }
        cookie.setPath("/");
        cookie.setMaxAge(0);
        resp.addCookie(cookie);
    }

    // 인증 관련 메서드들
    public Member getMember() {
        if (isLogin()) {
            if (member == null) {
                SecurityUser user = getUser();
                if (user != null) {
                    member = entityManager.find(Member.class, user.getId());
                }
            }
            return member;
        }
        return null;
    }

    public Member getActor() {
        return Optional.ofNullable(
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                )
                .map(Authentication::getPrincipal)
                .filter(principal -> principal instanceof SecurityUser)
                .map(principal -> (SecurityUser) principal)
                .map(securityUser -> securityUser.getUsername())
                .flatMap(memberService::findByUsername)
                .orElse(null);
    }

    public void setLogin(Member member) {
        UserDetails user = new SecurityUser(
                member.getId(),
                member.getUsername(),
                "",
                member.getNickname(),
                member.getAuthorities()
        );

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user,
                user.getPassword(),
                user.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public boolean isAdmin() {
        if (isLogout()) return false;

        if (isAdmin == null) {
            isAdmin = getUser()
                    .getAuthorities()
                    .stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        }

        return isAdmin;
    }

    public boolean isLogout() {
        return !isLogin();
    }

    public boolean isLogin() {
        if (isLogin == null) getUser();
        return isLogin;
    }

    private SecurityUser getUser() {
        if (isLogin == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof SecurityUser) {
                user = (SecurityUser) authentication.getPrincipal();
                isLogin = true;
            } else {
                user = null;
                isLogin = false;
            }
        }
        return user;
    }

    public void setLogout() {
        removeCrossDomainCookie("accessToken");
        removeCrossDomainCookie("refreshToken");
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    public boolean isFrontUrl(String url) {
        return url.startsWith(AppConfig.getSiteFrontUrl());
    }

    public void destroySession() {
        req.getSession().invalidate();
    }

    public void makeAuthCookies(String accessToken, String refreshToken) {
        setCrossDomainCookie("accessToken", accessToken);
        setCrossDomainCookie("refreshToken", refreshToken);
    }

    // getActor 와 다르게
    // 이 함수에서 리턴하는 것은 야매가 아니다.
    public Optional<Member> findByActor() {
        Member actor = getActor();

        if (actor == null) return Optional.empty();

        return memberService.findById(actor.getId());
    }

    public void deleteCookie(String name) {
        ResponseCookie cookie = ResponseCookie.from(name, null)
                .path("/")
                .domain("localhost")
                .sameSite("Strict")
                .secure(true)
                .httpOnly(true)
                .maxAge(0)
                .build();

        resp.addHeader("Set-Cookie", cookie.toString());
    }
}