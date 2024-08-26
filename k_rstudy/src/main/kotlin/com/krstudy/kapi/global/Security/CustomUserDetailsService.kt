package com.krstudy.kapi.global.Security

import com.krstudy.kapi.domain.member.repository.MemberRepository
import com.ll.medium.global.security.SecurityUser
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CustomUserDetailsService(
    private val memberRepository: MemberRepository
) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val member = memberRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("사용자를 찾을 수 없습니다.")

        return SecurityUser(
            id = member.id!!,  // member.id가 null이 아닌 경우만 이 부분이 실행됨
            username = member.username,
            password = member.password,
            authorities = member.authorities
        )
    }
}
