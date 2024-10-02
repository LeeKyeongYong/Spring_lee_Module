package com.krstudy.kapi.domain.post.service

import com.krstudy.kapi.domain.comment.repository.PostCommentRepository
import com.krstudy.kapi.domain.comment.entity.PostComment
import com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.domain.post.entity.Post
import com.krstudy.kapi.domain.post.entity.PostLike
import com.krstudy.kapi.domain.post.repository.PostRepository
import com.krstudy.kapi.domain.post.repository.PostlikeRepository
import com.krstudy.kapi.global.exception.GlobalException
import com.krstudy.kapi.global.exception.MessageCode
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Service
@Transactional(readOnly = true)
class PostService(
    private val postRepository: PostRepository,
    private val postCommentRepository: PostCommentRepository,
    private val postlikeRepository: PostlikeRepository
) {

    @Transactional
    fun write(author: Member, title: String, body: String, isPublished: Boolean): Post {
        val post = Post(author = author, title = title, body = body, isPublished = isPublished)
        return postRepository.save(post)
    }

    fun findTop30ByIsPublishedOrderByIdDesc(isPublished: Boolean): List<Post> {
        return postRepository.findTop30ByIsPublishedOrderByIdDesc(isPublished)
    }

    fun findById(id: Long): Optional<Post> {
        return postRepository.findById(id)
    }

    fun search(kw: String, pageable: Pageable): Page<Post> {
        return postRepository.search(null, true, kw, pageable)
    }

    fun search(author: Member?, isPublished: Boolean?, kw: String, pageable: Pageable): Page<Post> {
        return postRepository.search(author, isPublished, kw, pageable)
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
    fun like(member: Member, post: Post) {
        // 이미 좋아요를 눌렀는지 검사
        if (postlikeRepository.existsByPostAndMember(post, member)) {
            throw GlobalException(MessageCode.ALREADY_LIKED)
        }

        try {
            // 좋아요 추가 진행
            val postLike = PostLike(
                member = member,
                post = post
            )
            postlikeRepository.save(postLike)

            // 추가적으로 Post 객체의 좋아요 수를 업데이트하거나 필요한 작업 수행
            post.addLike(member) // 예시로 추가적인 좋아요 수 업데이트를 가정
        } catch (e: DataIntegrityViolationException) {
            // 경쟁 조건의 경우, 좋아요가 존재하는지 다시 확인
            if (postlikeRepository.existsByPostAndMember(post, member)) {
                throw GlobalException(MessageCode.ALREADY_LIKED)
            } else {
                throw e // 다른 무결성 위반이라면 예외를 다시 throw
            }
        }
    }


    @Transactional
    fun cancelLike(actor: Member, post: Post) {
        if (canCancelLike(actor, post)) {
            post.deleteLike(actor)
        }
    }

    @Transactional
    fun writeComment(actor: Member, post: Post, body: String): PostComment {
        val postComment = PostComment(author = actor, post = post, body = body)
        return postCommentRepository.save(postComment)
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

    // 리포지토리 메소드 호출
    fun findAuthorNameByPostId(postId: Long, authorId: Long): String {
        return postRepository.findAuthorNameByPostId(postId, authorId)
    }
}