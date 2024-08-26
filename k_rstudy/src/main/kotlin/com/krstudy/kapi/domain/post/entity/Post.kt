package com.krstudy.kapi.domain.post.entity

import com.krstudy.kapi.domain.comment.entity.PostComment
import com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.global.jpa.BaseEntity
import com.krstudy.kapi.domain.post.entity.PostLike
import jakarta.persistence.*
import jakarta.persistence.CascadeType.ALL
import jakarta.persistence.FetchType.LAZY
import jakarta.persistence.GenerationType.IDENTITY
import lombok.*
import java.util.ArrayList

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Setter
class Post(
    @OneToMany(mappedBy = "post", cascade = [ALL], orphanRemoval = true)
    @Builder.Default
    var likes: MutableList<PostLike> = ArrayList(),

    @OneToMany(mappedBy = "post", cascade = [ALL], orphanRemoval = true)
    @Builder.Default
    @OrderBy("id DESC")
    var comments: MutableList<PostComment> = ArrayList(),

    @ManyToOne(fetch = LAZY)
    var author: Member? = null,

    var title: String? = null,

    @Column(columnDefinition = "TEXT")
    var body: String? = null,

    var isPublished: Boolean = false,

    @Setter(AccessLevel.PROTECTED)
    var hit: Long = 0
) : BaseEntity() {

    fun increaseHit() {
        hit++
    }

    fun addLike(member: Member) {
        if (hasLike(member)) {
            return
        }

        likes.add(
            PostLike.builder()
                .post(this)
                .member(member)
                .build()
        )
    }

    fun hasLike(member: Member): Boolean {
        return likes.any { it.member == member }
    }

    fun deleteLike(member: Member) {
        likes.removeIf { it.member == member }
    }

    fun writeComment(actor: Member, body: String): PostComment {
        val postComment = PostComment.builder()
            .post(this)
            .author(actor)
            .body(body)
            .build()

        comments.add(postComment)

        return postComment
    }
}