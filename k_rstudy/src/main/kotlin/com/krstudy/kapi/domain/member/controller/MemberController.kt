package com.krstudy.kapi.domain.member.controller

import com.krstudy.kapi.com.krstudy.kapi.domain.member.datas.JoinForm
import com.krstudy.kapi.domain.member.service.MemberService
import com.krstudy.kapi.global.https.ReqData
import com.krstudy.kapi.global.https.RespData
import jakarta.validation.Valid
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import com.krstudy.kapi.domain.member.datas.RegistrationQueue
import com.krstudy.kapi.global.exception.ErrorCode
import lombok.extern.slf4j.Slf4j

@Slf4j
@Controller
@RequestMapping("/member")
class MemberController(
    private val memberService: MemberService,
    private val rq: ReqData,
    private val registrationQueue: RegistrationQueue
) {

    // Logger 인스턴스 생성: 로그를 기록하기 위해 사용
    private val log: Logger = LoggerFactory.getLogger(MemberController::class.java)

    /**
     * 회원 가입 페이지를 보여주는 메소드.
     * 이 메소드는 인증되지 않은 사용자만 접근할 수 있습니다.
     * @return 가입 페이지의 뷰 이름
     */
    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")
    fun showJoin(): String {
        log.info("showJoin() method called") // 메소드 호출 로그 기록
        return "domain/member/member/join" // 가입 페이지로의 경로 반환
    }

    /**
     * 회원 가입 요청을 처리하는 메소드.
     * 폼 데이터를 검증하고, 큐에 가입 요청을 추가한 후 성공 응답을 생성합니다.
     * @param joinForm 가입 폼 데이터
     * @return 가입 성공 후 리디렉션할 경로
     */
    @PreAuthorize("isAnonymous()")
    @PostMapping("/join")
    fun join(@Valid joinForm: JoinForm): String {
        log.info("join() method called with JoinForm: $joinForm") // 폼 데이터와 함께 메소드 호출 로그 기록

        // 가입 요청을 큐에 추가
        registrationQueue.enqueue(joinForm.userid, joinForm.username, joinForm.password)
        log.info("User with userid: ${joinForm.userid} enqueued successfully") // 큐에 추가된 사용자 로그 기록

        // 성공적인 응답 생성
        val successResponse: RespData<String> = RespData.of(
            resultCode = ErrorCode.SUCCESS.code,
            msg = ErrorCode.SUCCESS.message
        )

        // 리디렉션 또는 페이지 이동
        log.info("Redirecting to /member/login with success response: $successResponse") // 리디렉션 로그 기록
        return rq.redirectOrBack(
            rs = successResponse,
            path = "/member/login"
        )
    }

    /**
     * 로그인 페이지를 보여주는 메소드.
     * 이 메소드는 인증되지 않은 사용자만 접근할 수 있습니다.
     * @return 로그인 페이지의 뷰 이름
     */
    @GetMapping("/login")
    fun showLogin(): String {
        log.info("showLogin() method called") // 메소드 호출 로그 기록
        return "domain/member/member/login" // 로그인 페이지로의 경로 반환
    }
}
