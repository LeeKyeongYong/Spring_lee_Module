package com.krstudy.kapi.domain.member.service

import com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.global.app.AppConfig
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.util.*

@Service
class AuthTokenService {

    fun genToken(member: Member, expireSeconds: Long): String {
        val claims = Jwts.claims()
            .add("id", member.id)
            .add("username", member.username)
            .add("authorities", member.getAuthoritiesAsStringList())
            .build()

        val issuedAt = Date()
        val expiration = Date(issuedAt.time + 1000 * expireSeconds)

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(issuedAt)
            .setExpiration(expiration)
            .signWith(SignatureAlgorithm.HS256, AppConfig.getJwtSecretKeyOrThrow())
            .compact()
    }

    fun genAccessToken(member: Member): String {
        return genToken(member, AppConfig.accessTokenExpirationSec) // 접근 방식 변경
    }

    fun getDataFrom(token: String): Map<String, Any> {
        val payload = Jwts.parser()
            .setSigningKey(AppConfig.getJwtSecretKeyOrThrow())
            .build()
            .parseClaimsJws(token)
            .body

        return mapOf(
            "id" to payload.get("id", Integer::class.java),
            "username" to payload.get("username", String::class.java),
            "authorities" to payload.get("authorities", List::class.java)
        )
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parser().setSigningKey(AppConfig.getJwtSecretKeyOrThrow()).build().parseClaimsJws(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun genRefreshToken(): String {
        val random = SecureRandom()
        val bytes = ByteArray(10)
        random.nextBytes(bytes)
        return Base64.getUrlEncoder().encodeToString(bytes)
    }
}