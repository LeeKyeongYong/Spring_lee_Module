package org.study.jqboot.global.https;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class ReqData {
    package com.krstudy.kapi.global.https;

import com.krstudy.kapi.domain.member.entity.Member;
import com.krstudy.kapi.global.Security.SecurityUser;
import com.krstudy.kapi.global.app.AppConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

    @Component
    @RequestScope
    public class ReqData {
        private final HttpServletResponse resp;

        @PersistenceContext
        private EntityManager entityManager;
        private SecurityUser user;
        private Member member;
        private Boolean isLogin;
        private Boolean isAdmin;

        public ReqData(HttpServletResponse resp) {
            this.resp = resp;
        }

        private HttpServletRequest getReq() {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        }

        public void setHeader(String name, String value) {
            resp.setHeader(name, value);
        }

        public String getHeader(String name, String defaultValue) {
            String value = getReq().getHeader(name);
            return value != null ? value : defaultValue;
        }

        public void setStatusCode(int statusCode) {
            resp.setStatus(statusCode);
        }

        public boolean isApi() {
            String xRequestedWith = getReq().getHeader("X-Requested-With");
            return "XMLHttpRequest".equals(xRequestedWith);
        }

        public void setCookie(String name, String value, int maxAgeInSeconds) {
            String cookieDomain = AppConfig.getSiteCookieDomain();
            Cookie cookie = new Cookie(name, value);
            cookie.setPath("/");
            cookie.setDomain(cookieDomain);
            cookie.setMaxAge(maxAgeInSeconds);
            resp.addCookie(cookie);
        }

        public void setCookie(String name, String value) {
            setCookie(name, value, -1);
        }

        private String getSiteCookieDomain() {
            String cookieDomain = AppConfig.getSiteCookieDomain();
            return !"localhost".equals(cookieDomain) ? "." + cookieDomain : null;
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
            removeCookie(name);

            ResponseCookie cookie = ResponseCookie.from(name, "")
                    .path("/")
                    .maxAge(0)
                    .domain(getSiteCookieDomain())
                    .secure(true)
                    .httpOnly(true)
                    .build();

            resp.addHeader("Set-Cookie", cookie.toString());
        }

        public Cookie getCookie(String name) {
            Cookie[] cookies = getReq().getCookies();
            if (cookies == null) return null;

            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
            return null;
        }

        public String getCookieValue(String name, String defaultValue) {
            Cookie cookie = getCookie(name);
            return cookie != null ? cookie.getValue() : defaultValue;
        }

        private long getCookieAsLong(String name, int defaultValue) {
            String value = getCookieValue(name, "");
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }

        public void removeCookie(String name) {
            Cookie cookie = getCookie(name);
            if (cookie != null) {
                cookie.setPath("/");
                cookie.setMaxAge(0);
                resp.addCookie(cookie);
            }
        }

        public Member getMember() {
            if (isLogout()) return null;

            if (member == null) {
                member = entityManager.getReference(Member.class, getUser().getId());
            }

            return member;
        }

        public boolean isAdmin() {
            if (isLogout()) return false;

            if (isAdmin == null) {
                isAdmin = getUser().getAuthorities().stream()
                        .anyMatch(auth -> "ROLE_ADMIN".equals(auth.getAuthority()));
            }

            return member != null && member.isAdmin();
        }

        public boolean isLogout() {
            return !isLogin();
        }

        public boolean isLogin() {
            if (isLogin == null) {
                getUser();
            }
            return Boolean.TRUE.equals(isLogin);
        }

        public void setLogin(SecurityUser securityUser) {
            if (securityUser == null) {
                throw new IllegalArgumentException("SecurityUser cannot be null");
            }
            SecurityContextHolder.getContext().setAuthentication(securityUser.genAuthentication());
        }

        public SecurityUser getUser() {
            if (isLogin == null) {
                user = Optional.ofNullable(SecurityContextHolder.getContext())
                        .map(context -> context.getAuthentication())
                        .filter(auth -> auth.getPrincipal() instanceof SecurityUser)
                        .map(auth -> (SecurityUser) auth.getPrincipal())
                        .orElse(null);

                isLogin = user != null;
            }

            return user;
        }

        public void setLogout() {
            removeCrossDomainCookie("accessToken");
            removeCrossDomainCookie("refreshToken");
            SecurityContextHolder.getContext().setAuthentication(null);
        }

        public void destroySession() {
            getReq().getSession().invalidate();
        }

        public String getCurrentUrlPath() {
            return getReq().getRequestURI();
        }

        public void setAttribute(String key, Object value) {
            getReq().setAttribute(key, value);
        }

        public String redirect(String url, String msg) {
            String[] urlBits = url.split("#", 2);
            String newUrl = urlBits[0];
            String encodedMsg = URLEncoder.encode(msg, StandardCharsets.UTF_8);

            StringBuilder sb = new StringBuilder();
            sb.append("redirect:");
            sb.append(newUrl);

            if (!encodedMsg.isEmpty()) {
                sb.append("?msg=");
                sb.append(encodedMsg);
            }

            if (urlBits.length == 2) {
                sb.append("#");
                sb.append(urlBits[1]);
            }

            return sb.toString();
        }

        public String getCurrentQueryStringWithoutParam(String paramToExclude) {
            String queryString = getReq().getQueryString();
            if (queryString == null) return "";

            String[] params = queryString.split("&");
            StringBuilder result = new StringBuilder();

            for (String param : params) {
                if (!param.startsWith(paramToExclude + "=")) {
                    if (result.length() > 0) {
                        result.append("&");
                    }
                    result.append(param);
                }
            }

            return result.length() > 0 ? "?" + result : "";
        }

        public boolean isFrontUrl(String url) {
            return url.startsWith(AppConfig.getSiteFrontUrl());
        }
}
