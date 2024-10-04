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