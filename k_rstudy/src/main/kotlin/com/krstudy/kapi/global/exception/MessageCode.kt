package com.krstudy.kapi.global.exception

enum class MessageCode(val code: String, val message: String) {

    // 인증 관련 오류
    UNAUTHORIZED("401-1", "인증된 사용자가 아닙니다."),
    UNAUTHORIZED_LOGIN_REQUIRED("401-2", "로그인이 필요합니다."),
    LOGIN_DISABLED_USER("401-3", "로그인할 수 없는 아이디입니다."), // 사용자가 탈퇴하면 N으로 변경하거나 관리자가 정지시키는 용도... 관리자는 그냥 통으로 지운다.

    // 리소스 관련 오류
    NOT_FOUND_POST("404-1", "해당 글이 존재하지 않습니다."),
    NOT_FOUND_COMMENT("404-2", "해당 댓글이 존재하지 않습니다."),
    NOT_FOUND_USER("404-3", "존재하지 않는 사용자입니다."),
    NOT_FOUND_RESOURCE("404-4", "해당 리소스가 존재하지 않습니다."),
    DUPLICATED_USERID("409", "이미 사용 중인 사용자 ID입니다."), // 중복 사용자 ID 에러 코드 추가

    // 권한 관련 오류
    FORBIDDEN("403-1", "권한이 없습니다."),

    // 요청 관련 오류
    BAD_REQUEST("400-1", "잘못된 요청입니다."),
    EMPTY_COMMENT_BODY("400-2", "댓글 내용이 비어 있습니다."),

    // 서버 오류
    INTERNAL_SERVER_ERROR("500-1", "서버 내부 오류입니다."),
    TOKEN_GENERATION_ERROR("500-2", "토큰 생성 중 오류가 발생했습니다."), // 추가한 토큰 생성 오류 코드

    // 성공적인 응답
    SUCCESS("200-1", "요청이 성공적으로 처리되었습니다."),
    ALREADY_LIKED("400-3", "이미 좋아요를 눌렀습니다."),

    // OAuth 관련 오류
    NOT_SUPPORTED_OAUTH_VENDOR("400-4", "해당 OAuth2 벤더는 지원되지 않습니다."),
    EXPIRED_VERIFICATION_CODE("400-5", "인증 코드가 만료되었습니다."), // 만료된 인증 코드 에러 추가
}