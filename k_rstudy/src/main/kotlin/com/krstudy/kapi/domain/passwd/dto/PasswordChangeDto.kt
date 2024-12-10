package com.krstudy.kapi.domain.passwd.dto

import org.springframework.web.multipart.MultipartFile
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.AssertTrue

data class PasswordChangeDto(
    @field:NotBlank(message = "현재 비밀번호는 필수입니다")
    val currentPassword: String,

    @field:NotBlank(message = "새 비밀번호는 필수입니다")
    @field:Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
        message = "비밀번호는 8자 이상이며, 영문자, 숫자, 특수문자를 포함해야 합니다"
    )
    val newPassword: String,

    @field:NotBlank(message = "비밀번호 확인은 필수입니다")
    val confirmPassword: String,

    @field:NotBlank(message = "변경 사유는 필수입니다")
    val changeReason: String,

    val signature: MultipartFile? = null
) {
    @AssertTrue(message = "새 비밀번호와 비밀번호 확인이 일치해야 합니다")
    fun isPasswordMatch(): Boolean = newPassword == confirmPassword
}