package com.krstudy.kapi.com.krstudy.kapi.domain.post.entity

import com.krstudy.kapi.com.krstudy.kapi.domain.comment.entity.PostComment
import com.krstudy.kapi.com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.com.krstudy.kapi.global.jpa.BaseEntity
import com.krstudy.kapi.domain.post.entity.PostLike
import jakarta.persistence.*
import lombok.*;
import kotlin.collections.ArrayList


@Entity
class Post(
    @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL], orphanRemoval = true)
    var likes: MutableList<PostLike> = ArrayList(),

    @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL], orphanRemoval = true)
    @OrderBy("id DESC")
    var comments: MutableList<PostComment> = ArrayList(),

    @ManyToOne(fetch = FetchType.LAZY)
    var author: Member? = null,

    var title: String? = null,

    @Column(columnDefinition = "TEXT")
    var body: String? = null,

    var isPublished: Boolean = false,

    private var hit: Long = 0
) : BaseEntity() {

    fun increaseHit() {
        hit++
    }

    fun addLike(member: Member) {
        if (hasLike(member)) {
            return
        }
        likes.add(PostLike.builder()
            .post(this)
            .member(member)
            .build())
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

    companion object {
        @JvmStatic
        fun builder() = PostBuilder()
    }

    class PostBuilder {
        private var likes: MutableList<PostLike> = ArrayList()
        private var comments: MutableList<PostComment> = ArrayList()
        private var author: Member? = null
        private var title: String? = null
        private var body: String? = null
        private var isPublished: Boolean = false
        private var hit: Long = 0

        fun likes(likes: MutableList<PostLike>) = apply { this.likes = likes }
        fun comments(comments: MutableList<PostComment>) = apply { this.comments = comments }
        fun author(author: Member?) = apply { this.author = author }
        fun title(title: String?) = apply { this.title = title }
        fun body(body: String?) = apply { this.body = body }
        fun isPublished(isPublished: Boolean) = apply { this.isPublished = isPublished }
        fun hit(hit: Long) = apply { this.hit = hit }

        fun build() = Post(likes, comments, author, title, body, isPublished, hit)
    }
}