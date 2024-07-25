package com.krstudy.kapi.domain.post.repository

import com.krstudy.kapi.com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.com.krstudy.kapi.domain.post.entity.Post
import com.krstudy.kapi.com.krstudy.kapi.domain.post.entity.QPost
import com.krstudy.kapi.com.krstudy.kapi.domain.post.repository.PostRepositoryCustom
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository
import com.querydsl.core.types.dsl.*

@Repository
class PostRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : PostRepositoryCustom {

    override fun search(author: Member?, isPublished: Boolean?, kw: String, pageable: Pageable): Page<Post> {
        // 조건 생성
        var condition: BooleanExpression = QPost.post.isNotNull()

        author?.let {
            condition = condition.and(QPost.post.author.eq(author))
        }

        isPublished?.let {
            condition = condition.and(QPost.post.isPublished.eq(it))
        }

        if (kw.isNotBlank()) {
            condition = condition.and(
                QPost.post.title.containsIgnoreCase(kw)
                    .or(QPost.post.body.containsIgnoreCase(kw))
            )
        }

        val postsQuery = jpaQueryFactory
            .selectFrom(QPost.post)
            .where(condition)

        pageable.sort.forEach { sortOrder ->
            val pathBuilder = PathBuilder(Post::class.java, QPost.post.metadata)
            val path = pathBuilder.get<Comparable<*>>(sortOrder.property) // Ensure proper type handling

            postsQuery.orderBy(
                OrderSpecifier(
                    if (sortOrder.isAscending) Order.ASC else Order.DESC,
                    path
                )
            )
        }

        postsQuery
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        // 전체 개수를 가져오기 위한 쿼리
        val totalQuery = jpaQueryFactory
            .select(QPost.post.count())
            .from(QPost.post)
            .where(condition)

        return PageableExecutionUtils.getPage(postsQuery.fetch(), pageable, totalQuery::fetchCount)
    }
}