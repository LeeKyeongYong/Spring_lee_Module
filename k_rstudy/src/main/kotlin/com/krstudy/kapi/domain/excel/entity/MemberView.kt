package com.krstudy.kapi.domain.excel.entity

import com.krstudy.kapi.global.jpa.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "member_view")
class MemberView(

    @Column(name = "member_id")
    val memberId: Long? = null,

    @Column(name = "userid")
    val userid: String? = null,

    @Column(name = "username")
    val username: String? = null,

    @Column(name = "role_type")
    val roleType: String? = null,

    @Column(name = "password")
    val password: String? = null,

    @Column(name = "authorities")
    val authorities: String? = null,

    @Column(name = "isAdmin")
    val isAdmin: Boolean? = null
) : BaseEntity() {}