package com.krstudy.kapi.domain.post.entity

import com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.global.jpa.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne

@Entity
data class PostLike(
    @ManyToOne var post: Post? = null,
    @ManyToOne var member: Member? = null
) : BaseEntity()
