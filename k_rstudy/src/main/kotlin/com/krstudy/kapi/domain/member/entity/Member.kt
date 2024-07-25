package com.krstudy.kapi.com.krstudy.kapi.domain.member.entity

import com.krstudy.kapi.com.krstudy.kapi.domain.member.datas.M_Role
import com.krstudy.kapi.com.krstudy.kapi.global.jpa.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Transient
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

@Entity
class Member : BaseEntity() {

    @Column(nullable = false)
    var username: String = ""

    @Column(nullable = false)
    var password: String = ""

    @Column(nullable = true)
    var roleType: String? = null

    @get:Transient
    val authorities: Collection<GrantedAuthority>
        get() {
            //val role: M_Role = M_Role.fromRoleType(roleType)
           // val authorities: MutableCollection<GrantedAuthority> = ArrayList()
            val authorities: MutableCollection<GrantedAuthority> = ArrayList()

            // roleType에 따라 권한 추가
            val role: M_Role = M_Role.values().find { it.authority == roleType } ?: M_Role.MEMBER
            authorities.add(SimpleGrantedAuthority(role.authority))

            // username에 따라 admin 역할 추가
            if (username == "system" || username == "admin") {
                authorities.add(SimpleGrantedAuthority(M_Role.ADMIN.authority))
            }

            return authorities
        }

    val isAdmin: Boolean
        get() = authorities.any { auth -> auth.authority == M_Role.ADMIN.authority }
}