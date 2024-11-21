package com.krstudy.kapi.domain.kafkaproducer.rw

import com.krstudy.kapi.domain.kafkaproducer.entity.WebLog
import org.springframework.batch.item.Chunk
import org.springframework.batch.item.ItemWriter
import org.springframework.kafka.core.KafkaTemplate

class WebLogItemWriter(
    private val kafkaTemplate: KafkaTemplate<String, WebLog>
) : ItemWriter<WebLog> {

    override fun write(items: Chunk<out WebLog>) {
        for (webLog in items) {
            kafkaTemplate.send("web_log_topic", webLog)
        }
    }
}