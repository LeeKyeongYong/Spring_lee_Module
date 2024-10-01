package com.krstudy.kapi.domain.oauth2.service

import com.krstudy.kapi.com.krstudy.kapi.domain.oauth2.dto.OAuthAttributes
import com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.domain.member.repository.MemberRepository
import lombok.RequiredArgsConstructor
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class CustomOAuth2UserService(
    private val memberRepository: MemberRepository,
    private val oAuthAttributesAdapterFactory: OAuthAttributesAdapterFactory
) : OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private val defaultOAuth2UserService = DefaultOAuth2UserService()

    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = defaultOAuth2UserService.loadUser(userRequest)

        val registrationId = userRequest.clientRegistration.registrationId
        val attributes = oAuthAttributes(registrationId, oAuth2User)
        val member = saveOrUpdate(attributes)

        return DefaultOAuth2User(
            listOf(SimpleGrantedAuthority(member.roleKey())),
            attributes.attributes,
            attributes.nameAttributeKey()
        )
    }

    private fun oAuthAttributes(registrationId: String, oAuth2User: OAuth2User): OAuthAttributes {
        return oAuthAttributesAdapterFactory.factory(registrationId)
            .toOAuthAttributes(oAuth2User.attributes)
    }

    private fun saveOrUpdate(attributes: OAuthAttributes): Member {
        val member = memberRepository.findByUserid(attributes.email)
            .map { it.update(attributes.name, attributes.picture) }
            .orElse(attributes.toEntity())

        return memberRepository.save(member)
    }
}