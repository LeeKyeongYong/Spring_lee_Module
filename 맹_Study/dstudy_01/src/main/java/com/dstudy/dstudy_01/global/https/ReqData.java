package com.dstudy.dstudy_01.global.https;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

@Component
@RequestScope
@RequiredArgsConstructor
public class ReqData {
    private final HttpServletResponse resp;

    @PersistenceContext
    private EntityManager entityManager;

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

        String result = Arrays.stream(queryString.split("&"))
                .filter(param -> !param.startsWith(paramToExclude + "="))
                .reduce((a, b) -> a + "&" + b)
                .orElse("");

        return result.isEmpty() ? "" : "?" + result;
    }

}