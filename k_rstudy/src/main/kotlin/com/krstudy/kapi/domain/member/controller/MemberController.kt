package com.krstudy.kapi.com.krstudy.kapi.domain.member.controller

import com.krstudy.kapi.com.krstudy.kapi.domain.member.service.MemberService
import com.krstudy.kapi.com.krstudy.kapi.global.https.ReqData
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/member")
class MemberController(
    private val memberService: MemberService,
    private val rq: ReqData
) {

    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")
    fun showJoin(): String {
        return "domain/member/member/join"
    }

    @Validated
    data class JoinForm(
        @field:NotBlank val username: String,
        @field:NotBlank val password: String
    )

    @PreAuthorize("isAnonymous()")
    @PostMapping("/join")
    fun join(@Valid joinForm: JoinForm): String {
        val joinRs = memberService.join(joinForm.username, joinForm.password)
        return rq.redirectOrBack(joinRs, "/member/login")
    }

    @GetMapping("/login")
    fun showLogin(): String {
        return "domain/member/member/login"
    }
}