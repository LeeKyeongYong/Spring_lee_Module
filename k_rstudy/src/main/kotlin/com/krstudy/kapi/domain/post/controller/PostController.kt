package com.krstudy.kapi.domain.post.controller



import com.krstudy.kapi.domain.post.entity.Post
import com.krstudy.kapi.global.exception.GlobalException
import com.krstudy.kapi.global.https.ReqData
import com.krstudy.kapi.domain.post.service.PostService
import com.krstudy.kapi.global.exception.ErrorCode
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView

@Controller
@RequestMapping("/post")
class PostController(
    private val postService: PostService,
    private val rq: ReqData
) {
    @GetMapping("/{id}")
    fun showDetail(@PathVariable id: Long): String {
        val post = postService.findById(id).orElseThrow { GlobalException(ErrorCode.NOT_FOUND_POST) }
        postService.increaseHit(post)
        rq.setAttribute("post", post)
        return "domain/post/post/detail"
    }

    @GetMapping("/list")
    fun showList(
        @RequestParam(defaultValue = "") kw: String,
        @RequestParam(defaultValue = "1") page: Int
    ): String {
        val sorts = listOf(Sort.Order.desc("id"))
        val pageable: Pageable = PageRequest.of(page - 1, 10, Sort.by(sorts))
        val postPage: Page<Post> = postService.search(kw, pageable)
        rq.setAttribute("postPage", postPage)
        rq.setAttribute("page", page)
        return "domain/post/post/list"
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myList")
    fun showMyList(
        @RequestParam(defaultValue = "") kw: String,
        @RequestParam(defaultValue = "1") page: Int
    ): String {
        val sorts = listOf(Sort.Order.desc("id"))
        val pageable: Pageable = PageRequest.of(page - 1, 10, Sort.by(sorts))
        val member = rq.getMember() ?: throw GlobalException(ErrorCode.UNAUTHORIZED)
        val postPage: Page<Post> = postService.search(member, null, kw, pageable)
        rq.setAttribute("postPage", postPage)
        rq.setAttribute("page", page)
        return "domain/post/post/myList"
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    fun showWrite(): String = "domain/post/post/write"

    data class WriteForm(
        @field:NotBlank val title: String,
        @field:NotBlank val body: String,
        val isPublished: Boolean
    )

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    fun write(@Valid @ModelAttribute form: WriteForm, redirectAttributes: RedirectAttributes): RedirectView {
        val member = rq.getMember() ?: throw GlobalException(ErrorCode.UNAUTHORIZED)
        val post = postService.write(member, form.title, form.body, form.isPublished)
        redirectAttributes.addFlashAttribute("message", "${post.id}번 글이 작성되었습니다.")
        return RedirectView("/post/${post.id}")
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/modify")
    fun showModify(@PathVariable id: Long, model: Model): String {
        val post = postService.findById(id).orElseThrow { GlobalException(ErrorCode.NOT_FOUND_POST) }
        val member = rq.getMember() ?: throw GlobalException(ErrorCode.UNAUTHORIZED)
        if (!postService.canModify(member, post)) throw GlobalException(ErrorCode.FORBIDDEN)
        model.addAttribute("post", post)
        return "domain/post/post/modify"
    }

    data class ModifyForm(
        @field:NotBlank val title: String,
        @field:NotBlank val body: String,
        val isPublished: Boolean
    )

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}/modify")
    fun modify(@PathVariable id: Long, @Valid @ModelAttribute form: ModifyForm, redirectAttributes: RedirectAttributes): RedirectView {
        val post = postService.findById(id).orElseThrow { GlobalException(ErrorCode.NOT_FOUND_POST) }
        val member = rq.getMember() ?: throw GlobalException(ErrorCode.UNAUTHORIZED)
        if (!postService.canModify(member, post)) throw GlobalException(ErrorCode.FORBIDDEN)
        postService.modify(post, form.title, form.body, form.isPublished)
        redirectAttributes.addFlashAttribute("message", "${post.id}번 글이 수정되었습니다.")
        return RedirectView("/post/${post.id}")
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}/delete")
    fun delete(@PathVariable id: Long, redirectAttributes: RedirectAttributes): RedirectView {
        val post = postService.findById(id).orElseThrow { GlobalException(ErrorCode.NOT_FOUND_POST) }
        val member = rq.getMember() ?: throw GlobalException(ErrorCode.UNAUTHORIZED)
        if (!postService.canDelete(member, post)) throw GlobalException(ErrorCode.FORBIDDEN)
        postService.delete(post)
        redirectAttributes.addFlashAttribute("message", "${post.id}번 글이 삭제되었습니다.")
        return RedirectView("/post/list")
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/like")
    fun like(@PathVariable id: Long, redirectAttributes: RedirectAttributes): RedirectView {
        val post = postService.findById(id).orElseThrow { GlobalException(ErrorCode.NOT_FOUND_POST) }
        val member = rq.getMember() ?: throw GlobalException(ErrorCode.UNAUTHORIZED)
        if (!postService.canLike(member, post)) throw GlobalException(ErrorCode.FORBIDDEN)
        postService.like(member, post)
        redirectAttributes.addFlashAttribute("message", "${post.id}번 글을 추천하였습니다.")
        return RedirectView("/post/${post.id}")
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}/cancelLike")
    fun cancelLike(@PathVariable id: Long, redirectAttributes: RedirectAttributes): RedirectView {
        val post = postService.findById(id).orElseThrow { GlobalException(ErrorCode.NOT_FOUND_POST) }
        val member = rq.getMember() ?: throw GlobalException(ErrorCode.UNAUTHORIZED)
        if (!postService.canCancelLike(member, post)) throw GlobalException(ErrorCode.FORBIDDEN)
        postService.cancelLike(member, post)
        redirectAttributes.addFlashAttribute("message", "${post.id}번 글을 추천취소하였습니다.")
        return RedirectView("/post/${post.id}")
    }
}
