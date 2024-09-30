package com.krstudy.kapi.global.app

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "history-service")
interface HistoryFeignClient {

    @PostMapping("/v1/history/save")
    fun saveHistory(@RequestBody paramMap: Map<String, Any>): String
}