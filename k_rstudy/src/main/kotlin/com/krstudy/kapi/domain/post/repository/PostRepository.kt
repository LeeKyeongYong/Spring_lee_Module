package com.krstudy.kapi.domain.post.repository

import com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.domain.post.entity.Post
import org.hibernate.query.Page

import org.springframework.data.jpa.repository.JpaRepository


interface PostRepository : JpaRepository<Post, Long>, PostRepositoryCustom {
    fun findTop30ByIsPublishedOrderByIdDesc(isPublished: Boolean): List<Post>

}