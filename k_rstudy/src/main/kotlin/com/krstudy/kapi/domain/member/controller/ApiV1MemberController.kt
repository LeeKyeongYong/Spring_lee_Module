package com.krstudy.kapi.domain.member.controller

import com.krstudy.kapi.domain.member.service.MemberService
import com.krstudy.kapi.global.https.ReqData
import lombok.RequiredArgsConstructor
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User
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

    private val logger = LoggerFactory.getLogger(ApiV1MemberController::class.java)

    companion object {
        private const val DEFAULT_REDIRECT_URL = "/"
    }

    @GetMapping("/socialLogin/{providerTypeCode}")
    fun socialLogin(@RequestParam(required = false) redirectUrl: String?, @PathVariable providerTypeCode: String): String {
        redirectUrl?.takeIf { rq.isFrontUrl(it) }?.let {
            rq.setCookie("redirectUrlAfterSocialLogin", it, 60 * 10)
        }
        return "redirect:/oauth2/authorization/$providerTypeCode"
    }


}


/*

@PostMapping("/login")
    fun login(@Valid @RequestBody body: LoginRequestBody): RespData<LoginResponseBody> {
        val authAndMakeTokensRs = memberService.authAndMakeTokens(
            body.username,
            body.password
        ) ?: throw GlobalException(MessageCode.TOKEN_GENERATION_ERROR.code, MessageCode.TOKEN_GENERATION_ERROR.message)

        val refreshToken = authAndMakeTokensRs.data?.refreshToken
            ?: throw GlobalException(MessageCode.TOKEN_GENERATION_ERROR.code, MessageCode.TOKEN_GENERATION_ERROR.message)
        val accessToken = authAndMakeTokensRs.data?.accessToken
            ?: throw GlobalException(MessageCode.TOKEN_GENERATION_ERROR.code, MessageCode.TOKEN_GENERATION_ERROR.message)

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
        val member = rq.getMember() ?: return RespData.fromErrorCode(MessageCode.UNAUTHORIZED_LOGIN_REQUIRED) // 로그인 필요 시 에러 반환

        return RespData.of(
            resultCode = MessageCode.SUCCESS.code, // SUCCESS 코드 사용
            msg = MessageCode.SUCCESS.message, // SUCCESS 메시지 사용
            data = MeResponseBody(MemberDto.from(member)) // MemberDto 변환
        )
    }

    @PostMapping("/logout")
    fun logout(): RespData<Empty> {
        rq.setLogout()

        return RespData.of(MessageCode.SUCCESS.code, "로그아웃 성공") // ErrorCode.SUCCESS 사용
    }

 */