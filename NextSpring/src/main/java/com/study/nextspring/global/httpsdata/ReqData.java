package com.study.nextspring.global.httpsdata;

import com.study.nextspring.domain.member.entity.Member;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Optional;

    @Component
    @RequestScope
    @RequiredArgsConstructor
    public class ReqData {
        @Autowired private final HttpServletRequest req;
        @Autowired private final HttpServletResponse resp;
        @Autowired private final CookieService cookieService;
        @PersistenceContext
        private EntityManager entityManager;
        private SecurityUser user;
        private Member member;
        private Boolean isLogin;
        private Boolean isAdmin;


        public void setHeader(String name, String value) {
            resp.setHeader(name, value);
        }

        public String getHeader(String name, String defaultValue) {
            String value = req.getHeader(name);

            return value != null ? value : defaultValue;
        }

        public void setStatusCode(int statusCode) {
            resp.setStatus(statusCode);
        }

        public String getCurrentUrlPath() {
            return req.getRequestURI();
        }

        public boolean isApi() {
            String xRequestedWith = req.getHeader("X-Requested-With");
            return "XMLHttpRequest".equals(xRequestedWith);
        }


        public void setCookie(String name, String value, int maxAge) {
            cookieService.setCookie(name, value, maxAge);
        }

        public void setCookie(String name, String value) {
            cookieService.setCookie(name, value);
        }

        private String getSiteCookieDomain() {
            String cookieDomain = AppConfig.getSiteCookieDomain();

            if (!cookieDomain.equals("localhost")) {
                return cookieDomain = "." + cookieDomain;
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

            cookieService.removeCrossDomainCookie(name);
        }

        public Cookie getCookie(String name) {
            return cookieService.getCookie(name);
        }

        public String getCookieValue(String name, String defaultValue) {
            return cookieService.getCookieValue(name, defaultValue);
        }

        private long getCookieAsLong(String name, int defaultValue) {
            String value = getCookieValue(name, null);

            if (value == null) {
                return defaultValue;
            }

            return Long.parseLong(value);
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


        public Member getMember() {
            if (isLogin()) {
                if (member == null) {
                    SecurityUser user = getUser();
                    if (user != null) {
                        member = entityManager.find(Member.class, user.getId());
                    }
                }
                return member;
            } else {
                return null;
            }
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

        public void setLogin(SecurityUser securityUser) {
            SecurityContextHolder.getContext().setAuthentication(securityUser.genAuthentication());
            this.user = securityUser;
            this.isLogin = true;
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
}
