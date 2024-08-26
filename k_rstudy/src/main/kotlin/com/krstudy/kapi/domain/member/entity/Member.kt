package com.krstudy.kapi.domain.member.entity;

import com.krstudy.kapi.domain.member.datas.M_Role
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Transient
import com.krstudy.kapi.global.jpa.BaseEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

@Entity
class Member : BaseEntity() {

    @Column(nullable = false, unique = true) // 유니크 제약 조건 추가
    var username: String = ""

    @Column(nullable = false)
    var password: String = ""

    @Column(nullable = true)
    var roleType: String? = null

    @get:Transient
    val authorities: Collection<GrantedAuthority>
        get() {
            val authorities = mutableListOf<GrantedAuthority>()

            // roleType에 따라 권한 추가
            val role: M_Role = M_Role.values().find { it.authority == roleType } ?: M_Role.MEMBER
            authorities.add(SimpleGrantedAuthority(role.authority))

            // username에 따라 admin 역할 추가
            if (username.equals("system", ignoreCase = true) || username.equals("admin", ignoreCase = true)) {
                authorities.add(SimpleGrantedAuthority(M_Role.ADMIN.authority))
            }

            return authorities
        }

    val isAdmin: Boolean
        get() = authorities.any { auth -> auth.authority.equals(M_Role.ADMIN.authority) }
}