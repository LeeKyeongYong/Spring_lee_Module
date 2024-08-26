package com.krstudy.kapi.domain.post.entity


import com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.domain.post.entity.Post
import com.krstudy.kapi.global.jpa.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne
import lombok.Builder

@Entity
class PostLike private constructor(
    @ManyToOne var post: Post? = null,
    @ManyToOne var member: Member? = null
) : BaseEntity() {
    companion object {
        fun builder() = Builder()
    }

    class Builder {
        private var post: Post? = null
        private var member: Member? = null

        fun post(post: Post) = apply { this.post = post }
        fun member(member: Member) = apply { this.member = member }
        fun build() = PostLike(post, member)
    }
}