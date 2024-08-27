package com.krstudy.kapi.domain.member.service

import com.krstudy.kapi.com.krstudy.kapi.global.exception.CustomException
import com.krstudy.kapi.domain.member.datas.M_Role
import com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.domain.member.repository.MemberRepository
import com.krstudy.kapi.global.exception.ErrorCode
import com.krstudy.kapi.global.https.RespData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.Executors

@Service
@Transactional(readOnly = true)
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder
) {
    @Transactional
    suspend fun join(userid: String, username: String, password: String, role: String): RespData<Member> {
        val existingMember = findByUsername(userid)
        if (existingMember != null) {
            return RespData.fromErrorCode(ErrorCode.UNAUTHORIZED)
        }

        // role 매개변수를 M_Role의 authority와 정확히 일치하는지 확인하고 설정
        val roleType = M_Role.values().find { it.authority.equals(role, ignoreCase = true) }?.authority
            ?: run {
                // role이 null이거나 빈 문자열인 경우 처리
                if (userid.equals("system", ignoreCase = true) || userid.equals("admin", ignoreCase = true)) {
                    M_Role.ADMIN.authority
                } else {
                    M_Role.MEMBER.authority
                }
            }

        val member = Member().apply {
            this.userid = userid
            this.username = username
            this.password = passwordEncoder.encode(password)
            this.roleType = roleType
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

    fun findByUsername(userid: String): Member? {
        return memberRepository.findByUserid(userid)
    }

    fun count(): Long {
        return memberRepository.count()
    }
}