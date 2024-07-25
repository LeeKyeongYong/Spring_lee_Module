package com.krstudy.kapi.com.krstudy.kapi.domain.post.repository

import com.krstudy.kapi.com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.com.krstudy.kapi.domain.post.entity.Post
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.core.types.Order;
import com.querydsl.jpa.impl.JPAQueryFactory
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import com.ll.medium.domain.post.post.entity.QPost.post

@RequiredArgsConstructor
class PostRepositoryImpl(private val jpaQueryFactory: JPAQueryFactory) : PostRepositoryCustom {

    override fun search(isPublished: Boolean, kw: String?, pageable: Pageable): Page<Post> {

        // 조건 생성
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

        for (o in pageable.sort) {
            val pathBuilder = PathBuilder(post.type, post.metadata)
            postsQuery.orderBy(OrderSpecifier(if (o.isAscending) Order.ASC else Order.DESC, pathBuilder.get(o.property)))
        }

        postsQuery
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        // 전체 개수를 가져오기 위한 쿼리
        val totalQuery = jpaQueryFactory
            .select(post.count())
            .from(post)
            .where(condition)

        return PageableExecutionUtils.getPage(postsQuery.fetch(), pageable, totalQuery::fetchCount)
    }

    override fun search(author: Member, isPublished: Boolean?, kw: String?, pageable: Pageable): Page<Post> {
        // 조건 생성
        var condition: BooleanExpression = post.author.eq(author)

        isPublished?.let {
            condition = condition.and(post.isPublished.eq(it))
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

        for (o in pageable.sort) {
            val pathBuilder = PathBuilder(post.type, post.metadata)
            postsQuery.orderBy(OrderSpecifier(if (o.isAscending) Order.ASC else Order.DESC, pathBuilder.get(o.property)))
        }

        postsQuery
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        // 전체 개수를 가져오기 위한 쿼리
        val totalQuery = jpaQueryFactory
            .select(post.count())
            .from(post)
            .where(condition)

        return PageableExecutionUtils.getPage(postsQuery.fetch(), pageable, totalQuery::fetchCount)
    }
}
