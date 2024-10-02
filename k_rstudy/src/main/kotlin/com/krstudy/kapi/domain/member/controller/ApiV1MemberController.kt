package com.krstudy.kapi.domain.member.controller

import com.krstudy.kapi.domain.member.datas.LoginRequestBody
import com.krstudy.kapi.domain.member.datas.LoginResponseBody
import com.krstudy.kapi.domain.member.datas.MeResponseBody
import com.krstudy.kapi.domain.member.dto.MemberDto
import com.krstudy.kapi.domain.member.service.MemberService
import com.krstudy.kapi.global.exception.ErrorCode
import com.krstudy.kapi.global.exception.GlobalException
import com.krstudy.kapi.global.https.ReqData
import com.krstudy.kapi.global.https.RespData
import com.krstudy.kapi.standard.base.Empty
import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Transactional(readOnly = true)
class ApiV1MemberController(
    private val memberService: MemberService,
    private val rq: ReqData
) {

    @PostMapping("/login")
    fun login(@Valid @RequestBody body: LoginRequestBody): RespData<LoginResponseBody> {
        val authAndMakeTokensRs = memberService.authAndMakeTokens(
            body.username,
            body.password
        ) ?: throw GlobalException(ErrorCode.TOKEN_GENERATION_ERROR.code, ErrorCode.TOKEN_GENERATION_ERROR.message)

        val refreshToken = authAndMakeTokensRs.data?.refreshToken
            ?: throw GlobalException(ErrorCode.TOKEN_GENERATION_ERROR.code, ErrorCode.TOKEN_GENERATION_ERROR.message)
        val accessToken = authAndMakeTokensRs.data?.accessToken
            ?: throw GlobalException(ErrorCode.TOKEN_GENERATION_ERROR.code, ErrorCode.TOKEN_GENERATION_ERROR.message)

        rq.setCrossDomainCookie("refreshToken", refreshToken)
        rq.setCrossDomainCookie("accessToken", accessToken)

        return authAndMakeTokensRs.newDataOf(
            LoginResponseBody(
                MemberDto.from(authAndMakeTokensRs.data.member)
            )
        )
    }


    @GetMapping("/me")
    fun getMe(): RespData<MeResponseBody> {
        val member = rq.getMember() ?: return RespData.fromErrorCode(ErrorCode.UNAUTHORIZED_LOGIN_REQUIRED) // 로그인 필요 시 에러 반환

        return RespData.of(
            resultCode = ErrorCode.SUCCESS.code, // SUCCESS 코드 사용
            msg = ErrorCode.SUCCESS.message, // SUCCESS 메시지 사용
            data = MeResponseBody(MemberDto.from(member)) // MemberDto 변환
        )
    }

    @PostMapping("/logout")
    fun logout(): RespData<Empty> {
        rq.setLogout()

        return RespData.of(ErrorCode.SUCCESS.code, "로그아웃 성공") // ErrorCode.SUCCESS 사용
    }
}
