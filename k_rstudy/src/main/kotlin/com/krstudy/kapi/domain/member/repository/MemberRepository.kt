package com.krstudy.kapi.domain.member.repository

import com.krstudy.kapi.domain.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : JpaRepository<Member, Long> {
    fun findByUsername(username: String): Member?
    fun findByUserid(userid: String): Member?
}
