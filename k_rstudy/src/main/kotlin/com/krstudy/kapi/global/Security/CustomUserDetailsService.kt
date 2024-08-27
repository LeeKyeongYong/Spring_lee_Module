package com.krstudy.kapi.global.Security

import com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.domain.member.repository.MemberRepository
import com.krstudy.kapi.global.Security.SecurityUser
import lombok.RequiredArgsConstructor
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class CustomUserDetailsService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder
) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        println("username: "+username);
        val member = memberRepository.findByUserid(username)
            ?: throw UsernameNotFoundException("User not found: $username")

        return SecurityUser(
            id = member.id!!,
            username = member.userid,
            password = member.password,
            authorities = member.authorities
        )
    }

    fun authenticate(username: String, rawPassword: String): Boolean {
        val member = memberRepository.findByUserid(username)
            ?: return false

        return passwordEncoder.matches(rawPassword, member.password)
    }
}
