package com.study.nextspring.domain.member.service;

import com.study.nextspring.domain.member.entity.Member;
import com.study.nextspring.global.app.AppConfig;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthTokenService {

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                AppConfig.getJwtSecretKey().getBytes(StandardCharsets.UTF_8)
        );
    }

    // JWT 토큰 생성
    public String genToken(Member member, long expireSeconds) {
        Claims claims = Jwts.claims();
        claims.setSubject(member.getUsername());
        claims.setIssuer("NextSpring");
        claims.put("id", member.getId());  // add() 대신 put() 사용
        claims.put("username", member.getUsername());
        claims.put("authorities", member.getAuthoritiesAsStringList());

        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + 1000 * expireSeconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)  // 수정된 signWith
                .compact();
    }

    // 액세스 토큰 생성
    public String genAccessToken(Member member) {
        return genToken(member, AppConfig.getAccessTokenExpirationSec());
    }

    // 토큰에서 데이터 추출
    public Map<String, Object> getDataFrom(String token) {
        Claims payload = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())  // 수정된 setSigningKey
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Map.of(
                "id", payload.get("id", Integer.class),
                "username", payload.get("username", String.class),
                "authorities", payload.get("authorities", List.class)
        );
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())  // 수정된 setSigningKey
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 리프레시 토큰 생성
    public String genRefreshToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[10];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().encodeToString(bytes);
    }
}