package com.krstudy.kapi.domain.member.controller

import com.krstudy.kapi.com.krstudy.kapi.domain.member.datas.JoinForm
import com.krstudy.kapi.com.krstudy.kapi.global.exception.CustomException
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
import com.krstudy.kapi.global.lgexecution.LogExecutionTime
import lombok.extern.slf4j.Slf4j
import org.springframework.security.core.Authentication // Correct import
import org.springframework.security.core.context.SecurityContextHolder

@Slf4j
@Controller
@RequestMapping("/member")
class MemberController(
    private val memberService: MemberService, // 회원 서비스에 대한 의존성 주입
    private val rq: ReqData, // 요청 데이터에 대한 의존성 주입
    private val registrationQueue: RegistrationQueue // 가입 요청 큐에 대한 의존성 주입
) {

    // Logger 인스턴스 생성: 로그를 기록하기 위해 사용
    private val log: Logger = LoggerFactory.getLogger(MemberController::class.java)

    /**
     * 회원 가입 페이지를 보여주는 메소드.
     * 인증되지 않은 사용자만 접근할 수 있습니다.
     * @return 가입 페이지의 뷰 이름
     */
    @PreAuthorize("isAnonymous()") // 인증되지 않은 사용자만 접근 가능
    @GetMapping("/join") // GET 요청에 대해 /member/join 경로로 매핑
    @LogExecutionTime // 메소드 실행 시간 로그 기록
    fun showJoin(): String {
        log.info("showJoin() method called") // 메소드 호출 로그 기록
        return "domain/member/member/join" // 가입 페이지의 뷰 경로 반환
    }

    /**
     * 회원 가입 요청을 처리하는 메소드.
     * 폼 데이터를 검증하고, 큐에 가입 요청을 추가한 후 성공 응답을 생성합니다.
     * @param joinForm 가입 폼 데이터
     * @return 가입 성공 후 리디렉션할 경로
     */
    @PreAuthorize("isAnonymous()") // 인증되지 않은 사용자만 접근 가능
    @PostMapping("/join") // POST 요청에 대해 /member/join 경로로 매핑
    @LogExecutionTime // 메소드 실행 시간 로그 기록
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
     * 인증되지 않은 사용자만 접근할 수 있습니다.
     * @return 로그인 페이지의 뷰 이름
     */
    @GetMapping("/login") // GET 요청에 대해 /member/login 경로로 매핑
    @LogExecutionTime // 메소드 실행 시간 로그 기록
    fun showLogin(): String {
        log.info("showLogin() method called") // 메소드 호출 로그 기록
        return "domain/member/member/login" // 로그인 페이지의 뷰 경로 반환
    }

    /**
     * 로그인 처리 후의 경로를 반환하는 메소드.
     * 인증된 사용자에 대해 로그인 후 페이지를 반환합니다.
     * @return 로그인 처리 후의 페이지 경로
     */
    @GetMapping("/login/ok") // GET 요청에 대해 /member/login/ok 경로로 매핑
    @LogExecutionTime // 메소드 실행 시간 로그 기록
    fun logi2n(): String {
        log.info("logi2n() method called") // 메소드 호출 로그 기록
        val auth: Authentication? = SecurityContextHolder.getContext().authentication // 현재 인증 정보 가져오기
        val member = memberService.findByUsername(auth?.name ?: throw CustomException(ErrorCode.UNAUTHORIZED_LOGIN_REQUIRED)) // 사용자 정보를 가져옴

        // 로그인 검증
        memberService.validateLogin(member ?: throw CustomException(ErrorCode.NOT_FOUND_USER))

        // 로그인 후 페이지 반환
        return "domain/member/member/login"
    }
}
