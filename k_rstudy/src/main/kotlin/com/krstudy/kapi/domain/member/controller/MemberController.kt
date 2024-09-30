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
import org.springframework.ui.Model
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
    @GetMapping("/view_pdf.do")
    fun viewPdf(@RequestParam(value = "id") id: Long): ModelAndView {
        val mav = ModelAndView()
        mav.view = MemberPdfView()
        mav.addObject("member", memberService.getImageByNo(id))
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

    @GetMapping("/view.do")
    fun viewMemberByNo(@RequestParam(value = "id") id: Long, model: Model): String {
        model.addAttribute("member", memberService.getMemberByNo(id))
        return "domain/member/view" // 상세 페이지의 뷰 경로 반환
    }

    @GetMapping("/remove.do")
    fun removeMemberByNo(@RequestParam(value = "id") id: Long, attr: RedirectAttributes): String {
        memberService.removeMember(id)
        attr.addFlashAttribute("message", "회원이 삭제되었습니다.")
        return "redirect:/adm"
    }

}