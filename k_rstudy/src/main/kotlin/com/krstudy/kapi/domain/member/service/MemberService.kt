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
    private val executor = Executors.newFixedThreadPool(10) // 스레드 풀을 생성하여 비동기 작업 처리

    @Transactional
    suspend fun join(userid: String,username: String ,password: String, role: String): RespData<Member> {
        val existingMember = findByUsername(userid)
        if (existingMember != null) {
            return RespData.fromErrorCode(ErrorCode.UNAUTHORIZED)
        }

        // username에 따라 roleType을 결정
        val roleType = when {
            userid.equals("admin", ignoreCase = true) || userid.equals("system", ignoreCase = true) -> M_Role.ADMIN.authority
            role.isNotBlank() -> M_Role.values().find { it.authority.equals(role, ignoreCase = true) }?.authority ?: M_Role.MEMBER.authority
            else -> M_Role.MEMBER.authority
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