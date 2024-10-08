package com.krstudy.kapi.domain.member.repository

import com.krstudy.kapi.domain.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : JpaRepository<Member, Long> {
    fun findByUsername(username: String): Member?
    fun findByUserid(userid: String): Member?
    fun findByJwtToken(jwtToken: String): Member? // JWT 토큰으로 멤버 조회
    fun findByUsernameContaining(username: String): List<Member>  // 키워드로 사용자 검색
}
