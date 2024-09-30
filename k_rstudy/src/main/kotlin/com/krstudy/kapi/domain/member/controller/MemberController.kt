package com.krstudy.kapi.domain.member.controller

import com.krstudy.kapi.domain.member.datas.JoinForm
import com.krstudy.kapi.domain.member.datas.RegistrationQueue
import com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.domain.member.service.MemberService
import org.springframework.http.MediaType
import com.krstudy.kapi.global.exception.ErrorCode
import com.krstudy.kapi.global.https.ReqData
import com.krstudy.kapi.global.https.RespData
import com.krstudy.kapi.global.lgexecution.LogExecutionTime
import com.krstudy.kapi.standard.base.MemberPdfView
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import lombok.extern.slf4j.Slf4j
import org.springframework.web.bind.annotation.*
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.slf4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.ModelAndView
import org.springframework.http.HttpHeaders
import java.io.File
import java.io.FileNotFoundException


@Slf4j
@Controller
@RequestMapping("/member") // 회원 관련 요청을 처리하는 컨트롤러

class MemberController(
    private val memberService: MemberService, // 회원 서비스에 대한 의존성 주입
    private val rq: ReqData, // 요청 데이터에 대한 의존성 주입
    private val registrationQueue: RegistrationQueue, // 가입 요청 큐에 대한 의존성 주입
    private val passwordEncoder: PasswordEncoder // 비밀번호 인코더 주입
) {

    private val log: Logger = LoggerFactory.getLogger(MemberController::class.java) // Logger 인스턴스 생성

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
        return "domain/member/join" // 가입 페이지의 뷰 경로 반환
    }

    /**
     * 회원 가입 요청을 처리하는 메소드.
     * 폼 데이터를 검증하고, 큐에 가입 요청을 추가한 후 성공 응답을 생성합니다.
     * @param joinForm 가입 폼 데이터
     * @return 가입 성공 후 리디렉션할 경로
     */
    @PostMapping("/join")
    @LogExecutionTime
    suspend fun join(
        @Valid @ModelAttribute joinForm: JoinForm,
        bindingResult: BindingResult,
        redirectAttributes: RedirectAttributes
    ): String {
        if (bindingResult.hasErrors()) {
            bindingResult.allErrors.forEach { error ->
                val fieldName = (error as FieldError).field
                val errorMessage = error.defaultMessage
                redirectAttributes.addFlashAttribute("error_$fieldName", errorMessage)
            }
            return "redirect:/member/join"
        }

        if (memberService.findByUserid(joinForm.userid) != null) {
            redirectAttributes.addFlashAttribute("error_userid", "이미 등록된 사용자 ID입니다.")
            return "redirect:/member/join"
        }

        log.info("회원가입 시도: userid=${joinForm.userid}, username=${joinForm.username}, userEmail=${joinForm.userEmail}")

        val imageBytes = joinForm.image?.let { getImageBytes(it) } ?: getDefaultImageBytes()
        registrationQueue.enqueue(joinForm.userid, joinForm.username, joinForm.password, joinForm.userEmail, null, imageBytes)

        redirectAttributes.addFlashAttribute("userid", joinForm.userid)
        return "redirect:/member/login"
    }

    private fun getImageBytes(image: MultipartFile): ByteArray {
        return image.bytes
    }

    // 기본 이미지 바이트를 가져오는 함수
    private fun getDefaultImageBytes(): ByteArray {
        val inputStream = this::class.java.classLoader.getResourceAsStream("gen/images/notphoto.jpg")
            ?: throw FileNotFoundException("Default image not found in resources")

        return inputStream.readBytes().also {
            inputStream.close() // InputStream을 닫는 것을 잊지 마세요
        }
    }


    /**
     * 로그인 페이지를 보여주는 메소드.
     * 인증되지 않은 사용자만 접근할 수 있습니다.
     * @return 로그인 페이지의 뷰 이름
     */
    @GetMapping("/login") // GET 요청에 대해 /member/login 경로로 매핑
    @LogExecutionTime // 메소드 실행 시간 로그 기록
    fun showLogin(): String {
        return "domain/member/login" // 로그인 페이지의 뷰 경로 반환
    }

    /**
     * 회원의 PDF 정보를 보여주는 메소드.
     * @param id 회원 ID
     * @return ModelAndView 객체
     */
    @GetMapping("/view_pdf/{id}") // 특정 회원의 PDF 정보를 보여주는 GET 요청
    fun viewPdf(@PathVariable id: Long): ModelAndView {
        val member: Member? = memberService.getMemberByNo(id)

        // member가 null인지 확인
        if (member == null) {
            log.error("Member not found for id: $id") // 오류 로그 기록
            throw RuntimeException("Member not found") // 적절한 예외 처리
        }

        val mav = ModelAndView()
        mav.view = MemberPdfView() // PDF 뷰 설정
        mav.addObject("member", member) // member 객체 추가
        return mav
    }

    /**
     * 특정 회원의 이미지를 가져오는 메소드.
     * @param id 회원 ID
     * @return 이미지 바이트 배열을 포함한 ResponseEntity
     */
    @GetMapping("/image/{id}") // 특정 회원의 이미지를 가져오는 GET 요청
    fun getImage(@PathVariable id: Long): ResponseEntity<ByteArray> {
        val member: Member? = memberService.getImageByNo(id)


        // member가 null인지 확인
        if (member == null || member.image == null) {
            return ResponseEntity.notFound().build() // 404 응답 반환
        }

        val contentType = member.imageType ?: MediaType.APPLICATION_OCTET_STREAM_VALUE // 기본 MIME 타입 설정

        return ResponseEntity.ok()
            .contentType(MediaType.valueOf(contentType)) // 적절한 콘텐츠 타입 설정
            .body(member.image) // 이미지 바디 반환
    }
}

/*

    /**
     * 로그인 요청을 처리하는 메소드.
     * 사용자 인증을 수행하고, 비밀번호를 검증하며, 로그인 성공 시 세션에 사용자 정보를 저장합니다.
     * @param userid 사용자 아이디
     * @param password 사용자 비밀번호
     * @param httpSession HTTP 세션 객체
     * @return 로그인 성공 또는 실패 후 리디렉션 경로
     */
    @PostMapping("/login")
    @LogExecutionTime
    fun login(@RequestParam userid: String, @RequestParam password: String, httpSession: HttpSession): String {
        log.info("login() method called with userid: $userid")

        // Check if userid and password are null or empty
        if (userid.isNullOrEmpty() || password.isNullOrEmpty()) {
            log.error("Userid or password is null or empty")
            return "redirect:/member/login?failMsg=invalid_input"
        }

        return try {
            // 사용자 인증
            val member = memberService.findByUserid(userid) ?: throw CustomException(ErrorCode.NOT_FOUND_USER)

            // 비밀번호 검증
            if (!passwordEncoder.matches(password, member.password)) {
                throw CustomException(ErrorCode.BAD_REQUEST, "비밀번호가 일치하지 않습니다.")
            }

            // 로그인 성공 후 세션에 사용자 정보를 저장
            httpSession.setAttribute("loggedInUser", member)

            log.info("User $userid logged in successfully")
            "redirect:/member/login/ok"  // 로그인 후 리다이렉션 경로

        } catch (e: CustomException) {
            log.error("Login failed for userid: $userid", e)
            "redirect:/member/login?error=${e.errorCode.code}"  // 로그인 실패 시 리다이렉션
        }
    }


    /**
     * 로그인 처리 후의 경로를 반환하는 메소드.
     * 인증된 사용자에 대해 로그인 후 페이지를 반환합니다.
     * @return 로그인 처리 후의 페이지 경로
     */
    @GetMapping("/login/ok")
    @LogExecutionTime // 메소드 실행 시간 로그 기록
    fun logi2n(): String {
        log.info("logi2n() method called")

        // 현재 인증 정보 가져오기
        val auth: Authentication? = SecurityContextHolder.getContext().authentication
        val securityUser = auth?.principal as? SecurityUser ?: throw CustomException(ErrorCode.UNAUTHORIZED_LOGIN_REQUIRED)

        val userid = securityUser.username
        log.info("Authenticated username: $userid")

        // 사용자 정보를 가져옴
        val member = memberService.findByUserid(userid) ?: throw CustomException(ErrorCode.NOT_FOUND_USER)

        // 로그인 검증
        memberService.validateLogin(member)

        // 로그인 후 페이지 반환
        return "domain/member/login"
    }

 */