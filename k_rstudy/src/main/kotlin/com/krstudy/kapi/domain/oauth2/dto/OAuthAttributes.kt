package com.krstudy.kapi.com.krstudy.kapi.domain.oauth2.dto


import com.krstudy.kapi.domain.member.datas.M_Role
import com.krstudy.kapi.domain.member.entity.Member
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
data class OAuthAttributes(
    @Value("\${security.jwt.secret}")
    val secretKey: String,  // Use 'val' instead of 'lateinit'
    val attributes: Map<String, Any>,
    val nameAttributeKey: String,
    val name: String,
    val email: String,
    val picture: String,
    val registrationId: String
) {
    fun toEntity(): Member {
        return Member(
            userid = email,
            username = name,
            roleType = M_Role.MEMBER.authority,
            password = "",
            userEmail = email,
            jwtToken = generateJwtToken(email, secretKey),
            imageType = null,
            image = null,

        )
    }

    private fun generateJwtToken(userid: String): String {
        return Jwts.builder()
            .setSubject(userid)
            .setExpiration(Date(System.currentTimeMillis() + 600000))  // 10 minutes validity
            .signWith(SignatureAlgorithm.HS512, secretKey.toByteArray())
            .compact()
    }
}
