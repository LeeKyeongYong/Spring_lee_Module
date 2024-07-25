package com.krstudy.kapi.domain.post.repository

import com.krstudy.kapi.com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.com.krstudy.kapi.domain.post.entity.Post
import com.krstudy.kapi.com.krstudy.kapi.domain.post.repository.PostRepositoryCustom
import com.krstudy.kapi.domain.post.entity.QPost
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository

@Repository
class PostRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : PostRepositoryCustom {

    private val post = QPost.post

    override fun search(isPublished: Boolean, kw: String?, pageable: Pageable): Page<Post> {
        var condition: BooleanExpression = post.isPublished.eq(isPublished)

        if (!kw.isNullOrBlank()) {
            condition = condition.and(
                post.title.containsIgnoreCase(kw)
                    .or(post.body.containsIgnoreCase(kw))
            )
        }

        val postsQuery = jpaQueryFactory
            .selectFrom(post)
            .where(condition)

        pageable.sort.forEach { sortOrder ->
            val pathBuilder = PathBuilder(Post::class.java, post.metadata)
            postsQuery.orderBy(
                OrderSpecifier(
                    if (sortOrder.isAscending) Order.ASC else Order.DESC,
                    pathBuilder.get(sortOrder.property)
                )
            )
        }

        postsQuery
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        val totalQuery = jpaQueryFactory
            .select(post.count())
            .from(post)
            .where(condition)

        return PageableExecutionUtils.getPage(postsQuery.fetch(), pageable, totalQuery::fetchCount)
    }

    override fun search(author: Member, isPublished: Boolean?, kw: String?, pageable: Pageable): Page<Post> {
        var condition: BooleanExpression = post.author.eq(author)

        if (isPublished != null) {
            condition = condition.and(post.isPublished.eq(isPublished))
        }

        if (!kw.isNullOrBlank()) {
            condition = condition.and(
                post.title.containsIgnoreCase(kw)
                    .or(post.body.containsIgnoreCase(kw))
            )
        }

        val postsQuery = jpaQueryFactory
            .selectFrom(post)
            .where(condition)

        pageable.sort.forEach { sortOrder ->
            val pathBuilder = PathBuilder(Post::class.java, post.metadata)
            postsQuery.orderBy(
                OrderSpecifier(
                    if (sortOrder.isAscending) Order.ASC else Order.DESC,
                    pathBuilder.get(sortOrder.property)
                )
            )
        }

        postsQuery
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        val totalQuery = jpaQueryFactory
            .select(post.count())
            .from(post)
            .where(condition)

        return PageableExecutionUtils.getPage(postsQuery.fetch(), pageable, totalQuery::fetchCount)
    }
}
