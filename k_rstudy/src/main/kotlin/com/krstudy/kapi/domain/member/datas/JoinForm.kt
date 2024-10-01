package com.krstudy.kapi.domain.member.datas

import jakarta.validation.constraints.NotBlank
import org.springframework.validation.annotation.Validated
import org.springframework.web.multipart.MultipartFile

@Validated
data class JoinForm( // 회원 가입 폼 데이터 클래스: 입력 데이터 검증을 위한 클래스
    @field:NotBlank val userid: String, // 사용자 ID (빈 값 허용 안 함)
    @field:NotBlank val username: String, // 사용자 이름 (빈 값 허용 안 함)
    @field:NotBlank val password: String, // 비밀번호 (빈 값 허용 안 함)
    @field:NotBlank val userEmail: String, // 사용자 이메일 (빈 값 허용 안 함)
    val image: MultipartFile? // 이미지 파일
)
