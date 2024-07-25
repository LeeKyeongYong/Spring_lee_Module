package com.krstudy.kapi.domain.comment.controller

import com.krstudy.kapi.com.krstudy.kapi.global.exception.GlobalException
import com.krstudy.kapi.com.krstudy.kapi.global.https.ReqData

import com.krstudy.kapi.domain.post.service.PostService
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
        val member = rq.member ?: throw GlobalException("401-1", "인증된 사용자가 아닙니다.")
        val post = postService.findById(id).orElseThrow { GlobalException("404-1", "해당 글이 존재하지 않습니다.") }
        val body = form.body ?: throw GlobalException("400-1", "댓글 내용이 비어 있습니다.")

        val postComment = postService.writeComment(member, post, body)

        return rq.redirect("/post/$id#postComment-${postComment.id}", "댓글이 작성되었습니다.")
    }

    @GetMapping("/{commentId}/modify")
    @PreAuthorize("isAuthenticated()")
    fun showModify(
        @PathVariable id: Long,
        @PathVariable commentId: Long
    ): String {
        val member = rq.member ?: throw GlobalException("401-1", "인증된 사용자가 아닙니다.")
        val post = postService.findById(id).orElseThrow { GlobalException("404-1", "해당 글이 존재하지 않습니다.") }
        val postComment = postService.findCommentById(commentId).orElseThrow { GlobalException("404-1", "해당 댓글이 존재하지 않습니다.") }

        if (!postService.canModifyComment(member, postComment)) {
            throw GlobalException("403-1", "해당 댓글을 수정할 권한이 없습니다.")
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
        val member = rq.member ?: throw GlobalException("401-1", "인증된 사용자가 아닙니다.")
        val postComment = postService.findCommentById(commentId).orElseThrow { GlobalException("404-1", "해당 댓글이 존재하지 않습니다.") }
        val body = form.body ?: throw GlobalException("400-1", "댓글 내용이 비어 있습니다.")

        if (!postService.canModifyComment(member, postComment)) {
            throw GlobalException("403-1", "해당 댓글을 수정할 권한이 없습니다.")
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
        val member = rq.member ?: throw GlobalException("401-1", "인증된 사용자가 아닙니다.")
        val postComment = postService.findCommentById(commentId).orElseThrow { GlobalException("404-1", "해당 댓글이 존재하지 않습니다.") }

        if (!postService.canDeleteComment(member, postComment)) {
            throw GlobalException("403-1", "해당 댓글을 삭제할 권한이 없습니다.")
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