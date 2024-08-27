package com.krstudy.kapi.global.exception

import com.krstudy.kapi.global.exception.ErrorCode

class CustomException(
    val errorCode: ErrorCode,
    message: String? = null
) : RuntimeException(message ?: errorCode.message)
