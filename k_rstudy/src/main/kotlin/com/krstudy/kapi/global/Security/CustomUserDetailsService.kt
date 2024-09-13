package com.krstudy.kapi.global.Security

import com.krstudy.kapi.domain.member.service.MemberService
import com.krstudy.kapi.global.exception.CustomException
import com.krstudy.kapi.global.exception.ErrorCode
import org.hibernate.query.sqm.tree.SqmNode.log
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
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

        log.info("member.useYn: ${member.useYn}")
        if (!"Y".equals(member.useYn, ignoreCase = true)) {
            throw UsernameNotFoundException("redirect:/member/login?failMsg=${ErrorCode.LOGIN_DISABLED_USER}")
        }

        return SecurityUser(
            id = member.id ?: throw UsernameNotFoundException("User ID is null"),
            username = member.userid,
            password = member.password,
            authorities = member.authorities
        )
    }
}
