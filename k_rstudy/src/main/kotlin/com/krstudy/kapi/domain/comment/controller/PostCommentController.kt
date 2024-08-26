package com.krstudy.kapi.domain.comment.controller

import com.krstudy.kapi.global.exception.GlobalException
import com.krstudy.kapi.global.https.ReqData

import com.krstudy.kapi.domain.post.service.PostService
import com.krstudy.kapi.global.exception.ErrorCode
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/post/{id}/comment")
class PostCommentController(
    private val postService: PostService,
    private val rq: ReqData
) {

    @GetMapping("/write")
    @PreAuthorize("isAuthenticated()")
    fun write(
        @PathVariable id: Long,
        @Valid @ModelAttribute form: WriteForm
    ): String {
        val member = rq.member ?: throw  GlobalException(ErrorCode.UNAUTHORIZED)
        val post = postService.findById(id).orElseThrow { GlobalException( ErrorCode.NOT_FOUND_POST) }
        val body = form.body ?: throw GlobalException(ErrorCode.EMPTY_COMMENT_BODY)

        val postComment = postService.writeComment(member, post, body)

        return rq.redirect("/post/$id#postComment-${postComment.id}", "댓글이 작성되었습니다.")
    }

    @GetMapping("/{commentId}/modify")
    @PreAuthorize("isAuthenticated()")
    fun showModify(
        @PathVariable id: Long,
        @PathVariable commentId: Long
    ): String {
        val member = rq.member ?: throw GlobalException(ErrorCode.UNAUTHORIZED)
        val post = postService.findById(id).orElseThrow { GlobalException(ErrorCode.NOT_FOUND_POST) }
        val postComment = postService.findCommentById(commentId).orElseThrow { GlobalException(ErrorCode.NOT_FOUND_COMMENT) }

        if (!postService.canModifyComment(member, postComment)) {
            throw GlobalException(ErrorCode.FORBIDDEN)
        }

        rq.setAttribute("post", post)
        rq.setAttribute("postComment", postComment)

        return "domain/post/postComment/modify"
    }

    @PutMapping("/{commentId}/modify")
    @PreAuthorize("isAuthenticated()")
    fun modify(
        @PathVariable id: Long,
        @PathVariable commentId: Long,
        @Valid @ModelAttribute form: ModifyForm
    ): String {
        val member = rq.member ?: throw GlobalException(ErrorCode.UNAUTHORIZED)
        val postComment = postService.findCommentById(commentId).orElseThrow { GlobalException(ErrorCode.NOT_FOUND_POST) }
        val body = form.body ?: throw GlobalException(ErrorCode.EMPTY_COMMENT_BODY)

        if (!postService.canModifyComment(member, postComment)) {
            throw GlobalException(ErrorCode.FORBIDDEN)
        }

        postService.modifyComment(postComment, body)

        return rq.redirect("/post/$id#postComment-${postComment.id}", "댓글이 수정되었습니다.")
    }

    @DeleteMapping("/{commentId}/delete")
    @PreAuthorize("isAuthenticated()")
    fun delete(
        @PathVariable id: Long,
        @PathVariable commentId: Long
    ): String {
        val member = rq.member ?: throw GlobalException(ErrorCode.UNAUTHORIZED)
        val postComment = postService.findCommentById(commentId).orElseThrow { GlobalException(ErrorCode.NOT_FOUND_COMMENT) }

        if (!postService.canDeleteComment(member, postComment)) {
            throw GlobalException(ErrorCode.FORBIDDEN)
        }

        postService.deleteComment(postComment)

        return rq.redirect("/post/$id", "$commentId 번 댓글이 삭제되었습니다.")
    }

    data class WriteForm(
        @field:NotBlank
        var body: String? = null
    )

    data class ModifyForm(
        @field:NotBlank
        var body: String? = null
    )
}