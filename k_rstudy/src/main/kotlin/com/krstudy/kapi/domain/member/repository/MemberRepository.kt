package com.krstudy.kapi.com.krstudy.kapi.domain.member.repository

import com.krstudy.kapi.com.krstudy.kapi.domain.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {
    fun findByUsername(username: String): Member?
}
