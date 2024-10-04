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

                logger.debug("Processing OAuth2 user: $oauthId from provider: $providerTypeCode")

                val attributes = oAuth2User.attributes
                val (username, nickname, profileImgUrl) = extractUserInfo(providerTypeCode, attributes, oauthId)

                logger.info("Extracted user info - Username: $username, Nickname: $nickname")

                val emailRegex = Regex("""email=([^,}]+)""")
                val emailMatchResult = emailRegex.find(oauthId) // userInfo에서 이메일을 찾음
                val email = emailMatchResult?.groups?.get(1)?.value
                //val modifiedEmail = if (providerTypeCode == "NAVER") email?.substringBefore('@')?.plus("@") ?: "" else ""
                val modifiedEmail = when (providerTypeCode) {
                    "NAVER" -> email?.substringBefore('@') ?: "" // '@' 기호 없이 저장
                    else -> "" // 기타 경우에 대한 처리
                }

                val result = memberService.modifyOrJoin(
                    username = username,
                    nickname = nickname,
                    providerTypeCode = providerTypeCode,
                    imageBytes = getDefaultImageBytes(),
                    profileImgUrl = profileImgUrl,
                    userid = modifiedEmail
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
    ): Triple<String, String, String> {
        return when (providerTypeCode) {
            "KAKAO" -> {
                val properties = attributes["properties"] as? Map<String, Any>
                    ?: throw IllegalStateException("Kakao properties not found")
                Triple(
                    properties["nickname"] as? String ?: throw IllegalStateException("Kakao nickname not found"),
                    properties["nickname"] as? String ?: "",
                    properties["profile_image"] as? String ?: ""
                )
            }
            "NAVER" -> {
                val response = attributes["response"] as? Map<String, Any>
                    ?: throw IllegalStateException("Naver response not found")
                println("Naver response: $response")
                Triple(
                    response["name"] as? String ?: throw IllegalStateException("Naver name not found"),
                    response["nickname"] as? String ?: "",
                    response["profile_image"] as? String ?: ""//,

                )
            }
            "GOOGLE" -> {
                println("GOOGLE response: $attributes")
                Triple(
                    "${providerTypeCode}__$oauthId",
                    attributes["name"] as? String ?: "",
                    attributes["picture"] as? String ?: ""
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