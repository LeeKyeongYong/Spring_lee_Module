package com.krstudy.kapi.domain.member.controller

import com.krstudy.kapi.domain.member.service.MemberService
import com.krstudy.kapi.global.https.ReqData
import com.krstudy.kapi.global.https.RespData
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import com.krstudy.kapi.domain.member.datas.RegistrationQueue
import com.krstudy.kapi.global.exception.ErrorCode

@Controller
@RequestMapping("/member")
class MemberController(
    private val memberService: MemberService,
    private val rq: ReqData,
    private val registrationQueue: RegistrationQueue
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
        registrationQueue.enqueue(joinForm.username, joinForm.password)

        // 성공적인 응답 생성
        val successResponse: RespData<String> = RespData.of(
            resultCode = ErrorCode.SUCCESS.code,
            msg = "회원가입 요청이 큐에 추가되었습니다."
        )

        // 리디렉션 또는 페이지 이동
        return rq.redirectOrBack(
            rs = successResponse,
            path = "/member/login"
        )
    }

    @GetMapping("/login")
    fun showLogin(): String {
        return "domain/member/member/login"
    }
}