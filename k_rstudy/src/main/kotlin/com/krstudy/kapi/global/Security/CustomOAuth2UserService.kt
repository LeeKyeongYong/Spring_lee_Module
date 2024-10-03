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
import java.io.FileNotFoundException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@Service
@Transactional(readOnly = true)
class CustomOAuth2UserService(
    private val memberService: MemberService
) : DefaultOAuth2UserService() {

    @Transactional
    @Throws(OAuth2AuthenticationException::class)
    // 여기서 suspend로 메서드를 변경
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User = runBlocking {
        val oAuth2User = super.loadUser(userRequest)
        val oauthId = oAuth2User.name
        val providerTypeCode = userRequest.clientRegistration.registrationId.uppercase()

        var nickname = ""
        var profileImgUrl = ""
        var username = ""
        var attributesProperties: Map<String, Any>? = null

        val attributes = oAuth2User.attributes

        when (providerTypeCode) {
            "KAKAO" -> {
                attributesProperties = attributes["properties"] as? Map<String, Any>
                nickname = attributesProperties?.get("nickname") as? String ?: ""
                username =  attributesProperties?.get("nickname") as? String ?: ""//"${providerTypeCode}__$oauthId"
                profileImgUrl = attributesProperties?.get("profile_image") as? String ?: ""
            }
            "NAVER" -> {
                attributesProperties = attributes["response"] as? Map<String, Any>
                username = attributesProperties?.get("name") as? String ?: ""//"${providerTypeCode}__$oauthId"
                nickname = attributesProperties?.get("nickname") as? String ?: ""
                profileImgUrl = attributesProperties?.get("profile_image") as? String ?: ""
            }
            "GOOGLE" -> {
                nickname = attributes["name"] as? String ?: ""
                profileImgUrl = attributes["picture"] as? String ?: ""
                username = "${providerTypeCode}__$oauthId"
            }
        }

        println("OAuth Provider: $providerTypeCode, Username: $username, Nickname: $nickname, Profile Image URL: $profileImgUrl")

        //OAuth Provider: GOOGLE, Username: GOOGLE__107421080655407978839, Nickname: 이경용,
        // Profile Image URL: https://lh3.googleusercontent.com/a/ACg8ocLi0VKzgvfkUdnt2r85abhUhI0Iq_prtJwwuSzuEOD1C-qjzg=s96-c

        //OAuth Provider: NAVER, Username: NAVER__{id=NfvKCKanuKnKs_zezuz_t9FTmPuWYXiDnjP3LaKd0R8, nickname=푸차,
        // profile_image=https://phinf.pstatic.net/contact/20240925_220/1727259165483XvIJh_PNG/profileImage.png, age=30-39,
        // gender=M, email=sleekydz86@naver.com, mobile=010-3615-4287, mobile_e164=+821036154287, name=이경용, birthday=06-19, birthyear=1986},
        // Nickname: 푸차, Profile Image URL: https://phinf.pstatic.net/contact/20240925_220/1727259165483XvIJh_PNG/profileImage.png


        // 이 부분에서 suspend 함수 호출
        val member: Member = memberService.modifyOrJoin(
            username = username,
            nickname = nickname,
            providerTypeCode = providerTypeCode,
            imageBytes = getDefaultImageBytes(),
            profileImgUrl = profileImgUrl
        ).data ?: throw IllegalStateException("Failed to create or update member")

        println("Member 저장 완료: ID: ${member.id}, Username: ${member.username}")

        return@runBlocking SecurityUser(member.id!!, member.username!!, member.password!!, member.authorities)
    }

    private fun getDefaultImageBytes(): ByteArray {
        val inputStream = this::class.java.classLoader.getResourceAsStream("gen/images/notphoto.jpg")
            ?: throw FileNotFoundException("Default image not found in resources")

        return inputStream.use { it.readBytes() }
    }
}
