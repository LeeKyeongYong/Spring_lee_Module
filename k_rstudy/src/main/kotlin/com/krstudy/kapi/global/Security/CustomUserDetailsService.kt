package com.krstudy.kapi.global.Security


import com.krstudy.kapi.domain.member.repository.MemberRepository
import com.krstudy.kapi.global.exception.CustomException
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
    override fun loadUserByUsername(userid: String): UserDetails {
        val member = memberRepository.findByUserid(userid)
            ?: throw CustomException(ErrorCode.NOT_FOUND_USER)

        return SecurityUser(
            id = member.id!!,  // member.id가 null이 아닌 경우만 이 부분이 실행됨
            userid = member.userid ?: "",  // null인 경우 기본값을 설정
            username = member.username ?: "",  // null인 경우 기본값을 설정
            password = member.password,
            authorities = member.authorities
        )
    }
}
