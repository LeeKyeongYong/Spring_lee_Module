package com.krstudy.kapi.com.krstudy.kapi.domain.member.service

import com.krstudy.kapi.com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.com.krstudy.kapi.domain.member.repository.MemberRepository
import com.krstudy.kapi.com.krstudy.kapi.global.https.RespData
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
    fun join(username: String, password: String): RespData<Member> {
        val existingMember = findByUsername(username)
        if (existingMember != null) {
            return RespData.of("400-2", "이미 존재하는 회원입니다.")
        }

        val member = Member().apply {
            this.username = username
            this.password = passwordEncoder.encode(password)
        }
        memberRepository.save(member)

        return RespData.of(
            "200",
            "${member.username}님 환영합니다. 회원가입이 완료되었습니다. 로그인 후 이용해주세요.",
            member
        )
    }

    fun findByUsername(username: String): Member? {
        return memberRepository.findByUsername(username)
    }

    fun count(): Long {
        return memberRepository.count()
    }
}
