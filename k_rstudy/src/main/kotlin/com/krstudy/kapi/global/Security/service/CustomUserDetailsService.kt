package com.krstudy.kapi.global.Security.service

import com.krstudy.kapi.domain.member.service.MemberService
import com.krstudy.kapi.global.Security.SecurityUser
import com.krstudy.kapi.global.exception.MessageCode
import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CustomUserDetailsService(
    private val memberService: MemberService
) : UserDetailsService {

    private val log = LoggerFactory.getLogger(CustomUserDetailsService::class.java)

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val member = memberService.findByUserid(username)
            ?: throw UsernameNotFoundException("User not found: $username")

        log.info("member.useYn: ${member.useYn}")
        if (!"Y".equals(member.useYn, ignoreCase = true)) {
            throw UsernameNotFoundException(MessageCode.LOGIN_DISABLED_USER.message)
        }

        return SecurityUser(
            id = member.id ?: throw UsernameNotFoundException("User ID is null"),
            _username = member.userid,
            _password = member.password,
            authorities = member.authorities
        )
    }
}