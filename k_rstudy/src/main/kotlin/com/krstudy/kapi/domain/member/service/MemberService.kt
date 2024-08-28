package com.krstudy.kapi.domain.member.service

import com.krstudy.kapi.domain.member.datas.M_Role
import com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.domain.member.repository.MemberRepository
import com.krstudy.kapi.global.exception.CustomException
import com.krstudy.kapi.global.exception.ErrorCode
import com.krstudy.kapi.global.https.RespData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder
) {
    @Transactional
    suspend fun join(userid: String, username: String, userEmail: String,password: String, role: String): RespData<Member> {
        val existingMember = findByUserid(userid)
        if (existingMember != null) {
            return RespData.fromErrorCode(ErrorCode.UNAUTHORIZED)
        }

        val roleType = M_Role.values().find { it.authority.equals(role, ignoreCase = true) }?.authority
            ?: run {
                if (userid.equals("system", ignoreCase = true) || userid.equals("admin", ignoreCase = true)) {
                    M_Role.ADMIN.authority
                } else {
                    M_Role.MEMBER.authority
                }
            }

        val member = Member().apply {
            this.userid = userid
            this.username = username
            this.userEmail = userEmail
            this.password = passwordEncoder.encode(password)
            this.roleType = roleType
            this.useYn = "Y"
        }

        withContext(Dispatchers.IO) {
            memberRepository.save(member)
        }

        return RespData.of(
            ErrorCode.SUCCESS.code,
            "${member.userid}님 환영합니다. 회원가입이 완료되었습니다. 로그인 후 이용해주세요.",
            member
        )
    }

    fun findByUserid(userid: String): Member? {
        return memberRepository.findByUserid(userid)
    }

    fun findByUserName(username: String): Member {
        return memberRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found with username: $username")
    }
    fun count(): Long {
        return memberRepository.count()
    }

    fun validateLogin(member: Member) {
        if (member.useYn == "N") {
            throw CustomException(ErrorCode.LOGIN_DISABLED_USER, "로그인 비활성화된 사용자입니다.")
        }
    }
}