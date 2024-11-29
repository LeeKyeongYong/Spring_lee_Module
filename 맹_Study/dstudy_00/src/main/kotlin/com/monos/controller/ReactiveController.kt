package com.monos.controller


import com.monos.dto.User
import com.monos.handler.RequestHandler
import com.monos.service.UserService
import kotlinx.coroutines.flow.Flow
import org.springframework.http.MediaType
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import kotlinx.coroutines.reactor.asFlux

@RestController
class ReactiveController(
    private val requestHandler: RequestHandler,
    private val userService: UserService
) {
    // 사용자 Flow 처리
    @PostMapping("/request/flow")
    fun processUserFlow(@RequestBody userList: Flow<User>): Flow<User> {
        return requestHandler.handleUserFlowRequest(userList)
    }

    // 스트리밍 데이터 전송
    @GetMapping("/hello", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun streamUserData(): Flux<String> {
        return userService.createUserStream().asFlux() // 수정
    }

    // HTTP Body 데이터 처리
    @PostMapping("/process-data")
    suspend fun processData(
        httpReq: ServerHttpRequest,
        httpRes: ServerHttpResponse
    ): Mono<Void> {
        return requestHandler.handleDataBufferRequest(httpReq, httpRes)
    }
}
