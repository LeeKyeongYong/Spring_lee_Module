package com.krstudy.kapi.com.krstudy.kapi.global.exception

import com.krstudy.kapi.global.exception.CustomException
import com.krstudy.kapi.global.exception.GlobalException
import com.krstudy.kapi.global.https.ReqData
import com.krstudy.kapi.global.https.RespData
import com.krstudy.kapi.standard.base.Empty
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import java.io.PrintWriter
import java.io.StringWriter

@ControllerAdvice
@RequiredArgsConstructor
class GlobalExceptionHandler (private val rq: ReqData){

    @ExceptionHandler(CustomException::class)
    fun handleCustomException(ex: CustomException): ResponseEntity<Map<String, Any>> {
        val errorResponse = mapOf(
            "code" to ex.errorCode.code,
            "message" to ex.errorCode.message
        )
        return ResponseEntity(errorResponse, HttpStatus.valueOf(ex.errorCode.code.split("-")[0].toInt()))
    }
    // 자연스럽게 발생시킨 예외처리
    private fun handleApiException(ex: Exception): ResponseEntity<Any> {
        val body = LinkedHashMap<String, Any>().apply {
            put("resultCode", "500-1")
            put("statusCode", 500)
            put("msg", ex.localizedMessage)

            val data = LinkedHashMap<String, Any>()
            put("data", data)

            val sw = StringWriter()
            val pw = PrintWriter(sw)
            ex.printStackTrace(pw)
            data["trace"] = sw.toString().replace("\t", "    ").split(Regex("\\r\\n"))

            val path = rq.getCurrentUrlPath()
            data["path"] = path

            put("success", false)
            put("fail", true)
        }

        return ResponseEntity(body, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    // 개발자가 명시적으로 발생시킨 예외처리
    @ExceptionHandler(GlobalException::class)
    @ResponseStatus // 참고로 이 코드의 역할은 error 내용의 스키마를 타입스크립트화 하는데 있다.
    fun handle(ex: GlobalException): ResponseEntity<RespData<Empty>> {
        val status = HttpStatus.valueOf(ex.rsData.statusCode)
        rq.setStatusCode(ex.rsData.statusCode)

        return ResponseEntity(ex.rsData, status)
    }

    // 로깅 시 민감한 정보 마스킹
    private fun maskSensitiveInfo(info: String): String {
        return info.take(3) + "*".repeat(info.length - 3)
    }

}