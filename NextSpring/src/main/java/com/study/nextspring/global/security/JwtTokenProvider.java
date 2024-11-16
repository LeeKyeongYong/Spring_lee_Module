package com.study.nextspring.global.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${custom.jwt.secretKey}")
    private String secretKey;

    private final long expirationTime = 1000 * 60 * 60 * 24; // 24시간 유효

    // JWT 토큰 생성
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, secretKey)  // 여전히 signWith가 사용됨
                .compact();
    }

    // JWT 토큰에서 사용자 이름 추출
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()  // 변경된 부분
                .setSigningKey(secretKey)
                .build()  // builder 사용해야 함
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // JWT 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()  // 변경된 부분
                    .setSigningKey(secretKey)
                    .build()  // builder 사용해야 함
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException | ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
