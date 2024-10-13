package com.krstudy.kapi.com.krstudy.kapi.global.Security

import com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.domain.member.service.MemberService
import com.krstudy.kapi.global.Security.SecurityUser
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import java.io.FileNotFoundException
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.core.OAuth2Error

// Quadruple 데이터 클래스 정의
data class Quadruple<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)

@Service
@Transactional(readOnly = true)
class CustomOAuth2UserService(
    private val memberService: MemberService
) : DefaultOAuth2UserService() {

    private val logger = LoggerFactory.getLogger(CustomOAuth2UserService::class.java)

    @Transactional
    @Throws(OAuth2AuthenticationException::class)
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        return runBlocking {
            try {
                val oAuth2User = super@CustomOAuth2UserService.loadUser(userRequest)
                val oauthId = oAuth2User.name
                val providerTypeCode = userRequest.clientRegistration.registrationId.uppercase()

                println("sns로그인 response: $oAuth2User")

                logger.debug("Processing OAuth2 user: $oauthId from provider: $providerTypeCode")

                val attributes = oAuth2User.attributes
                val (username, nickname, profileImgUrl, email) = extractUserInfo(providerTypeCode, attributes, oauthId)

                logger.info("Extracted user info - Username: $username, Nickname: $nickname")

                val modifiedEmail = when (providerTypeCode) {
                    "NAVER", "KAKAO","GOOGLE" -> email?.substringBefore('@') ?: "" // '@' 기호 없이 저장
                    else -> email // 기타 경우 이메일 그대로 사용
                }

                val result = memberService.modifyOrJoin(
                    username = username,
                    nickname = nickname,
                    providerTypeCode = providerTypeCode,
                    imageBytes = getDefaultImageBytes(),
                    profileImgUrl = profileImgUrl,
                    userid = modifiedEmail,
                    userEmail = email
                )

                val member = result.data ?: throw IllegalStateException("Failed to create or update member: ${result.msg ?: "Unknown error"}")

                logger.info("Successfully processed member: ${member.id}")

                SecurityUser(
                    id = member.id ?: throw IllegalStateException("Member ID cannot be null"),
                    _username = member.username ?: throw IllegalStateException("Username cannot be null"),
                    _password = member.password ?: "",
                    authorities = listOf(SimpleGrantedAuthority(member.roleType ?: "ROLE_MEMBER"))
                )
            } catch (e: Exception) {
                logger.error("Error in loadUser", e)
                throw OAuth2AuthenticationException(OAuth2Error("authentication_failed"), e.message ?: "Authentication failed", e)
            }
        }
    }

    private fun extractUserInfo(
        providerTypeCode: String,
        attributes: Map<String, Any>,
        oauthId: String
    ): Quadruple<String, String, String, String> { // 이메일을 추가해서 Quadruple로 반환
        return when (providerTypeCode) {
            "KAKAO" -> {
                val properties = attributes["properties"] as? Map<String, Any>
                    ?: throw IllegalStateException("Kakao properties not found")
                val kakaoAccount = attributes["kakao_account"] as? Map<String, Any>
                    ?: throw IllegalStateException("Kakao account not found")
                val email = kakaoAccount["email"] as? String ?: ""

                Quadruple(
                    properties["nickname"] as? String ?: throw IllegalStateException("Kakao nickname not found"),
                    properties["nickname"] as? String ?: "",
                    properties["profile_image"] as? String ?: "",
                    email // 이메일 추가
                )
            }
            "NAVER" -> {
                val response = attributes["response"] as? Map<String, Any>
                    ?: throw IllegalStateException("Naver response not found")
                val email = response["email"] as? String ?: ""

                Quadruple(
                    response["name"] as? String ?: throw IllegalStateException("Naver name not found"),
                    response["nickname"] as? String ?: "",
                    response["profile_image"] as? String ?: "",
                    email // 이메일 추가
                )
            }
            "GOOGLE" -> {
                println("GOOGLE response: $attributes")

                val givenName = attributes["given_name"] as? String ?: throw IllegalStateException("Google given_name not found")
                val familyName = attributes["family_name"] as? String ?: ""
                // 이름을 합치기
                val fullName = "$familyName $givenName".trim() // trim()을 사용하여 불필요한 공백 제거


                Quadruple(
                    fullName as? String ?: throw IllegalStateException("Google name not found"),
                    attributes["name"] as? String ?: "",
                    attributes["picture"] as? String ?: "",
                    attributes["email"] as? String ?: "" // 이메일 추가
                )
            }
            else -> throw IllegalArgumentException("Unsupported provider: $providerTypeCode")
        }
    }

    private fun getDefaultImageBytes(): ByteArray {
        return try {
            val inputStream = this::class.java.classLoader.getResourceAsStream("gen/images/notphoto.jpg")
                ?: throw FileNotFoundException("Default image not found in resources")
            inputStream.use { it.readBytes() }
        } catch (e: Exception) {
            logger.error("Error loading default image", e)
            ByteArray(0) // Return empty array as fallback
        }
    }
}