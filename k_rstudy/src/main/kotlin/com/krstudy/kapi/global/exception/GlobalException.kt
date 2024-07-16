package com.krstudy.kapi.com.krstudy.kapi.global.exception

import com.krstudy.kapi.com.krstudy.kapi.global.https.RespData

class GlobalException(
    resultCode: String,
    msg: String
) : RuntimeException("$resultCode $msg") {

    val rsData: RespData<*> = RespData.of(resultCode, msg)
}