package com.krstudy.kapi.com.krstudy.kapi.domain.comment.repository

import com.krstudy.kapi.com.krstudy.kapi.domain.comment.entity.PostComment
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface PostCommentRepository : JpaRepository<PostComment, Long> {
    fun findCommentById(id: Long): Optional<PostComment>
}