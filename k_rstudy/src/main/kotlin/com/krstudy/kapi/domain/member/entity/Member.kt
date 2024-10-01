package com.krstudy.kapi.domain.member.entity

import com.krstudy.kapi.domain.member.datas.M_Role
import com.krstudy.kapi.global.jpa.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Lob
import jakarta.persistence.Transient
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

@Entity
class Member(
    @Column(nullable = false, unique = true)
    var userid: String = "",

    @Column(nullable = false)
    var username: String? = null,

    @Column(nullable = false)
    var roleType: String? = null,

    @Column(nullable = false)
    var password: String = "",

    @Column(nullable = false)
    var useYn: String = "Y",  // Y: 사용 가능, N: 사용 불가능

    @Column(nullable = false)
    var userEmail: String = "",

    @Column
    var jwtToken: String? = null,  // JWT 토큰 필드 추가

    @Column(name = "image_type")
    var imageType: String? = null, // 일반로그인

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    var image: ByteArray? = null, // 일반로그인

    @Column(length = 255) // 최대치
    var picture: String? = null, // oauth로그인

    @Transient
    private val roleStrategy: RoleStrategy = DefaultRoleStrategy()
) : BaseEntity() {

    @get:Transient
    val authorities: Collection<GrantedAuthority>
        get() = roleStrategy.getAuthorities(roleType, userid)

    val isAdmin: Boolean
        get() = authorities.any { auth -> auth.authority == M_Role.ADMIN.authority }

    @Transient
    fun getAuthoritiesAsStringList(): List<String> {
        val authorities = mutableListOf("ROLE_MEMBER")
        if (isAdmin) authorities.add("ROLE_ADMIN")
        return authorities
    }
}