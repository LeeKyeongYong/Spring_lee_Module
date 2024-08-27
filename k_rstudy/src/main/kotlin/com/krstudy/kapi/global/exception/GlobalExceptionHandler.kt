package com.krstudy.kapi.com.krstudy.kapi.global.exception

import com.krstudy.kapi.global.exception.CustomException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(CustomException::class)
    fun handleCustomException(ex: CustomException): ResponseEntity<Map<String, Any>> {
        val errorResponse = mapOf(
            "code" to ex.errorCode.code,
            "message" to ex.errorCode.message
        )
        return ResponseEntity(errorResponse, HttpStatus.valueOf(ex.errorCode.code.split("-")[0].toInt()))
    }
}