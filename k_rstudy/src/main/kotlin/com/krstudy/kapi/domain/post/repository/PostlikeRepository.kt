package com.krstudy.kapi.domain.post.repository

import com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.domain.post.entity.Post
import com.krstudy.kapi.domain.post.entity.PostLike
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface PostlikeRepository : JpaRepository<PostLike, Long> {
    fun existsByPostAndMember(post: Post, member: Member): Boolean
}
