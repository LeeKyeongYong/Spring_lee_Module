package com.krstudy.kapi.com.krstudy.kapi.global.exception

import com.krstudy.kapi.com.krstudy.kapi.global.https.RespData
import com.krstudy.kapi.global.exception.ErrorCode

class GlobalException(
    errorCode: ErrorCode
) : RuntimeException("${errorCode.code} ${errorCode.message}") {

    val rsData: RespData<Unit> = RespData.of(errorCode.code, errorCode.message)
}