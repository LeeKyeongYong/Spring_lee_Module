package com.krstudy.kapi.domain.member.dto

data class MemberInfo(
    val id: Long, // 이곳은 non-nullable이어야 합니다.
    val username: String?
)