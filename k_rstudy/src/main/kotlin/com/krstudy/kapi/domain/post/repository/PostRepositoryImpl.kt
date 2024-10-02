package com.krstudy.kapi.domain.post.repository

import com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.domain.post.repository.PostRepositoryCustom
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
import com.krstudy.kapi.domain.post.entity.*

@Repository
class PostRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : PostRepositoryCustom {

    override fun search(author: Member?, isPublished: Boolean?, kw: String, pageable: Pageable): Page<Post> {
        // 조건 생성
        var condition: BooleanExpression = QPost.post.isNotNull()

        // 검색 조건을 로그로 출력
        println("Searching with author: $author, isPublished: $isPublished, kw: $kw")

        // author가 null이 아닐 경우 조건 추가
        author?.let {
            condition = condition.and(QPost.post.author.eq(author))
        }

        // isPublished가 null이 아닐 경우 조건 추가
        isPublished?.let {
            condition = condition.and(QPost.post.isPublished.eq(it))
        }

        // kw가 비어있지 않을 경우 제목 또는 본문에서 검색
        if (kw.isNotBlank()) {
            condition = condition.and(
                QPost.post.title.containsIgnoreCase(kw)
                    .or(QPost.post.body.containsIgnoreCase(kw))
            )
        }

        // 쿼리 생성
        val postsQuery = jpaQueryFactory
            .selectFrom(QPost.post)
            .where(condition)

        // 페이지네이션 설정
        postsQuery
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        // 전체 개수를 가져오기 위한 쿼리
        val totalQuery = jpaQueryFactory
            .select(QPost.post.count())
            .from(QPost.post)
            .where(condition)

        // 페이지 결과 반환
        return PageableExecutionUtils.getPage(postsQuery.fetch(), pageable, totalQuery::fetchCount)
    }
}