package com.krstudy.kapi.com.krstudy.kapi.global.exception

import com.krstudy.kapi.global.exception.ErrorCode

class CustomException(val errorCode: ErrorCode) : RuntimeException(errorCode.message)