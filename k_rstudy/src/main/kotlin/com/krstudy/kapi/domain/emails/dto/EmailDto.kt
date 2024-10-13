package com.krstudy.kapi.domain.emails.dto

import java.time.LocalDateTime

data class EmailDto(
    var serviceEmail: String = "", // 기본 이메일 주소
    var customEmail: String? = null, // 파라미터로 받은 이메일 주소
    var title: String = "", // 메일 제목
    var content: String = "", // 메일 내용
    var receiverEmail: String = "", // 받는사람 이메일 주소
    var createDate: LocalDateTime? = null, // 보낸시간
    var modifyDate: LocalDateTime? = null // 수정시간
)