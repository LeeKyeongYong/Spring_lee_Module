package com.krstudy.kapi.com.krstudy.kapi.domain.post.repository

import com.krstudy.kapi.domain.post.entity.PostLike
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface PostlikeRepository : JpaRepository<PostLike, Long> {
}
