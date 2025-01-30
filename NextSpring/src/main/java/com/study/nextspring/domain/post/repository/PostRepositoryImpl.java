package com.study.nextspring.domain.post.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.nextspring.domain.member.entity.Member;
import com.study.nextspring.domain.post.entity.Post;
import com.study.nextspring.global.base.KwTypeV1;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import com.querydsl.core.BooleanBuilder;
import static com.study.nextspring.domain.post.entity.QPost.post;
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Post> findByKw(KwTypeV1 kwType, String kw, Member author, Boolean published, Boolean listed, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if (author != null) builder.and(post.author.eq(author));
        if (published != null) builder.and(post.published.eq(published));
        if (listed != null) builder.and(post.listed.eq(listed));

        if (kw != null && !kw.isBlank()) {
            applyKeywordFilter(kwType, kw, builder);
        }

        JPAQuery<Post> postsQuery = createPostsQuery(builder);
        applySorting(pageable, postsQuery);

        // offset과 limit을 설정
        postsQuery.offset(pageable.getOffset()).limit(pageable.getPageSize());

        JPAQuery<Long> totalQuery = createTotalQuery(builder);
        return PageableExecutionUtils.getPage(postsQuery.fetch(), pageable, totalQuery::fetchOne);
    }
    private void applyKeywordFilter(KwTypeV1 kwType, String kw, BooleanBuilder builder) {
        switch (kwType) {
            case TITLE -> builder.and(post.title.containsIgnoreCase(kw));
            case BODY -> builder.and(post.content.containsIgnoreCase(kw));
            case NAME -> builder.and(post.author.nickname.containsIgnoreCase(kw));
            case ALL -> builder.and(
                    post.title.containsIgnoreCase(kw)
                            .or(post.content.containsIgnoreCase(kw))
                            .or(post.author.nickname.containsIgnoreCase(kw))
            );
        }
    }

    private JPAQuery<Post> createPostsQuery(BooleanBuilder builder) {
        return jpaQueryFactory.select(post).from(post).where(builder);
    }

    private void applySorting(Pageable pageable, JPAQuery<Post> postsQuery) {
        for (Sort.Order order : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(post.getType(), post.getMetadata());
            postsQuery.orderBy(new OrderSpecifier(order.isAscending() ? Order.ASC : Order.DESC, pathBuilder.get(order.getProperty())));
        }
    }

    private JPAQuery<Long> createTotalQuery(BooleanBuilder builder) {
        return jpaQueryFactory.select(post.count()).from(post).where(builder);
    }
}