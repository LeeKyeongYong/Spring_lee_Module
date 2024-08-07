package com.krstudy.kapi.global.exception

enum class ErrorCode(val code: String, val message: String) {

    // 인증 관련 오류
    UNAUTHORIZED("401-1", "인증된 사용자가 아닙니다."),
    UNAUTHORIZED_LOGIN_REQUIRED("401-2", "로그인이 필요합니다."),

    // 리소스 관련 오류
    NOT_FOUND_POST("404-1", "해당 글이 존재하지 않습니다."),
    NOT_FOUND_COMMENT("404-2", "해당 댓글이 존재하지 않습니다."),
    NOT_FOUND_USER("404-3", "존재하지 않는 사용자입니다."),
    NOT_FOUND_RESOURCE("404-4", "해당 리소스가 존재하지 않습니다."),


    // 권한 관련 오류
    FORBIDDEN("403-1", "권한이 없습니다."),

    // 요청 관련 오류
    BAD_REQUEST("400-1", "잘못된 요청입니다."),
    EMPTY_COMMENT_BODY("400-2", "댓글 내용이 비어 있습니다."),

    // 서버 오류
    INTERNAL_SERVER_ERROR("500-1", "서버 내부 오류입니다.")
}
