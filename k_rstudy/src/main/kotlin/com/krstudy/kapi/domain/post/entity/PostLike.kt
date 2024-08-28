package com.krstudy.kapi.domain.post.entity

import com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.global.jpa.BaseEntity
import jakarta.persistence.*

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["post_id", "member_id"])])
class PostLike(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    val post: Post,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member
) : BaseEntity()