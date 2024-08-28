package com.krstudy.kapi.global.Security

import com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.domain.member.repository.MemberRepository
import com.krstudy.kapi.domain.member.service.MemberService
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
    private val memberService: MemberService
) : UserDetailsService {
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {

        val member = memberService.findByUserid(username)
            ?: throw UsernameNotFoundException("User not found: $username")

        return SecurityUser(
            id = member.id!!,
            username = member.userid,
            password = member.password,
            authorities = member.authorities
        )
    }

}
