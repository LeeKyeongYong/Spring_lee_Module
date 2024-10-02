package com.krstudy.kapi.global.exception

import com.krstudy.kapi.global.https.RespData
import com.krstudy.kapi.standard.base.Empty

open class GlobalException(
    errorCode: ErrorCode
) : RuntimeException("${errorCode.code} ${errorCode.message}") {

    // var로 선언하여 값을 변경할 수 있도록 함
    var rsData: RespData<Empty> = RespData.of(errorCode.code, errorCode.message)
        private set // 외부에서 설정할 수 없도록 설정

    constructor(msg: String) : this(ErrorCode.BAD_REQUEST) {  // BAD_REQUEST 예시
        this.rsData = RespData.of("400-0", msg)
    }

    constructor(resultCode: String, msg: String) : this(ErrorCode.BAD_REQUEST) {  // BAD_REQUEST 예시
        this.rsData = RespData.of(resultCode, msg)
    }

    class E404 : GlobalException(ErrorCode.NOT_FOUND_RESOURCE)  // NOT_FOUND_RESOURCE 예시
}