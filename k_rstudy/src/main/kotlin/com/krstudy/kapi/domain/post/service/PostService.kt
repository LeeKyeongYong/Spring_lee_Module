package com.krstudy.kapi.domain.post.service

import com.krstudy.kapi.com.krstudy.kapi.domain.comment.entity.PostComment
import com.krstudy.kapi.com.krstudy.kapi.domain.comment.repository.PostCommentRepository
import com.krstudy.kapi.com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.com.krstudy.kapi.domain.post.entity.Post
import com.krstudy.kapi.com.krstudy.kapi.domain.post.repository.PostRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Service
@Transactional(readOnly = true)
class PostService(
    private val postRepository: PostRepository,
    private val postCommentRepository: PostCommentRepository
) {

    @Transactional
    fun write(author: Member, title: String, body: String, isPublished: Boolean): Post {
        val post = Post(
            author = author,
            title = title,
            body = body,
            isPublished = isPublished
        )

        return postRepository.save(post)
    }

    fun findTop30ByIsPublishedOrderByIdDesc(isPublished: Boolean): List<Post> {
        return postRepository.findTop30ByIsPublishedOrderByIdDesc(isPublished)
    }

    fun findById(id: Long): Optional<Post> {
        return postRepository.findById(id)
    }

    fun search(kw: String, pageable: Pageable): Page<Post> {
        // Ensure 'true' is being used for 'isPublished' if it is a required value
        return postRepository.search(null, true, kw, pageable)
    }

    fun search(author: Member?, isPublished: Boolean?, kw: String, pageable: Pageable): Page<Post> {
        // Ensure to handle cases where parameters might be null
        return postRepository.search(author, isPublished, kw, pageable)
    }

    fun canLike(actor: Member?, post: Post): Boolean {
        return actor != null && !post.hasLike(actor)
    }

    fun canCancelLike(actor: Member?, post: Post): Boolean {
        return actor != null && post.hasLike(actor)
    }

    fun canModify(actor: Member?, post: Post): Boolean {
        return actor != null && actor == post.author
    }

    fun canDelete(actor: Member?, post: Post): Boolean {
        return actor != null && (actor.isAdmin || actor == post.author)
    }

    @Transactional
    fun modify(post: Post, title: String, body: String, published: Boolean) {
        post.title = title
        post.body = body
        post.isPublished = published
    }

    @Transactional
    fun delete(post: Post) {
        postRepository.delete(post)
    }

    @Transactional
    fun increaseHit(post: Post) {
        post.increaseHit()
    }

    @Transactional
    fun like(actor: Member, post: Post) {
        post.addLike(actor)
    }

    @Transactional
    fun cancelLike(actor: Member, post: Post) {
        post.deleteLike(actor)
    }

    @Transactional
    fun writeComment(actor: Member, post: Post, body: String): PostComment {
        return post.writeComment(actor, body)
    }

    fun canModifyComment(actor: Member?, comment: PostComment): Boolean {
        return actor != null && actor == comment.author
    }

    fun canDeleteComment(actor: Member?, comment: PostComment): Boolean {
        return actor != null && (actor.isAdmin || actor == comment.author)
    }

    fun findCommentById(id: Long): Optional<PostComment> {
        return postCommentRepository.findCommentById(id)
    }

    @Transactional
    fun modifyComment(postComment: PostComment, body: String) {
        postComment.body = body
    }

    @Transactional
    fun deleteComment(postComment: PostComment) {
        postCommentRepository.delete(postComment)
    }
}