package com.monos.handler

import com.monos.dto.User
import com.monos.service.UserService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.nio.charset.StandardCharsets

@Component
class RequestHandler(private val userService: UserService) {

    // HTTP Body를 읽어 처리
    suspend fun handleDataBufferRequest(
        httpReq: ServerHttpRequest,
        httpRes: ServerHttpResponse
    ): Mono<Void> {
        return httpReq.body
            .publishOn(Schedulers.parallel()) // 병렬 처리 스케줄러 사용
            .flatMap { dataBuffer ->
                Mono.fromCallable {
                    val byteArray = ByteArray(dataBuffer.readableByteCount())
                    dataBuffer.read(byteArray)
                    DataBufferUtils.release(dataBuffer)
                    String(byteArray, StandardCharsets.UTF_8)
                }
            }
            .doOnNext { processedData ->
                println("Received: $processedData") // 수신된 데이터 로그 출력
            }
            .then(Mono.empty())
    }

    // 사용자 Flow를 처리
    fun handleUserFlowRequest(userList: Flow<User>): Flow<User> {
        return userService.processUsers(userList)
    }
}