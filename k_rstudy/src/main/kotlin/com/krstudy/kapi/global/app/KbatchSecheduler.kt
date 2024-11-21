package com.krstudy.kapi.global.app

import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled


@Configuration
@EnableScheduling
class KbatchSecheduler {

    @Scheduled(fixedDelay = 10000)
    fun runBatchJob() {
        // 배치 작업 구현
    }
}