package com.krstudy.kapi.global.kafkaconsumer

import com.krstudy.kapi.domain.kafkaproducer.entity.WebLog
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class WebLogConsumer {
    @KafkaListener(topics = ["web_log_topic"], groupId = "web-log-group")
    fun consume(webLog: WebLog) {
        println("Received WebLog: $webLog")
    }
}