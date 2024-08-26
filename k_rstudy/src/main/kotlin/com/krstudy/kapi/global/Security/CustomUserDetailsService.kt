package com.krstudy.kapi.global.Security

import com.krstudy.kapi.com.krstudy.kapi.global.exception.CustomException
import com.krstudy.kapi.domain.member.repository.MemberRepository
import com.krstudy.kapi.global.exception.ErrorCode
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CustomUserDetailsService(
    private val memberRepository: MemberRepository
) : UserDetailsService {

    @Throws(CustomException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val member = memberRepository.findByUsername(username)
            ?: throw CustomException(ErrorCode.NOT_FOUND_USER)

        return SecurityUser(
            id = member.id!!,  // member.id가 null이 아닌 경우만 이 부분이 실행됨
            username = member.username,
            password = member.password,
            authorities = member.authorities
        )
    }
}