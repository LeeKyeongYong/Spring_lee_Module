package com.krstudy.kapi.domain.member.entity

import com.krstudy.kapi.domain.member.datas.M_Role
import com.krstudy.kapi.global.jpa.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Transient
import org.springframework.security.core.GrantedAuthority

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

    @Transient
    private val roleStrategy: RoleStrategy = DefaultRoleStrategy()
) : BaseEntity() {

    @get:Transient
    val authorities: Collection<GrantedAuthority>
        get() = roleStrategy.getAuthorities(roleType, userid)

    val isAdmin: Boolean
        get() = authorities.any { auth -> auth.authority.equals(M_Role.ADMIN.authority) }

    fun getRoleAuthorities(roleStrategy: RoleStrategy, userid: String): Collection<GrantedAuthority> {
        return roleStrategy.getAuthorities(roleType, userid)
    }
}