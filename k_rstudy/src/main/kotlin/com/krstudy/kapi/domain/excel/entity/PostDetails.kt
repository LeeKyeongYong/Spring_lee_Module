package com.krstudy.kapi.domain.excel.entity

import com.krstudy.kapi.global.jpa.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.Immutable
import java.time.LocalDateTime

@Entity
@Immutable
@IdClass(PostDetailsId::class)
@Table(name = "post_details")
data class PostDetails(
    @Id
    @Column(name = "post_id")
    val postId: Long? = null,

    @Id
    @Column(name = "comment_id")
    val commentId: Long? = null,

    @Column(name = "post_title")
    val postTitle: String? = null,

    @Column(name = "post_create_date")
    val postCreateDate: LocalDateTime? = null,

    @Column(name = "post_modify_date")
    val postModifyDate: LocalDateTime? = null,

    @Column(name = "post_hit")
    val postHit: Int? = null,

    @Column(name = "post_author_id")
    val postAuthorId: String? = null,

    @Column(name = "comment_author_id")
    val commentAuthorId: String? = null,

    @Column(name = "comment_create_date")
    val commentCreateDate: LocalDateTime? = null,

    @Column(name = "like_member_id")
    val likeMemberId: String? = null,

    @Column(name = "like_create_date")
    val likeCreateDate: LocalDateTime? = null
)