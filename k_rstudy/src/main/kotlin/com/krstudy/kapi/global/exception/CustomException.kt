package com.krstudy.kapi.global.exception

import com.krstudy.kapi.global.exception.ErrorCode
import org.springframework.security.core.AuthenticationException
class CustomException(
    val errorCode: ErrorCode
) : AuthenticationException(errorCode.message)
