package com.krstudy.kapi.global.app

import com.krstudy.kapi.domain.kafkaproducer.entity.WebLog
import com.krstudy.kapi.domain.trade.event.OrderEvent
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.*
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.support.serializer.JsonSerializer


@Configuration
@EnableKafka
class KafkaConfig {

    @Bean
    fun orderKafkaTemplate(
        producerFactory: ProducerFactory<String, OrderEvent>
    ): KafkaTemplate<String, OrderEvent> {
        return KafkaTemplate(producerFactory).apply {
            defaultTopic = "order-events"
        }
    }

    @Bean
    fun kafkaListenerContainerFactory(
        consumerFactory: ConsumerFactory<String, OrderEvent>
    ): ConcurrentKafkaListenerContainerFactory<String, OrderEvent> {
        return ConcurrentKafkaListenerContainerFactory<String, OrderEvent>().apply {
            this.consumerFactory = consumerFactory
            setConcurrency(3) // 컨슈머 스레드 수
            containerProperties.ackMode = ContainerProperties.AckMode.MANUAL_IMMEDIATE
        }
    }

    @Bean
    fun producerFactory(): ProducerFactory<String, WebLog> {
        val configProps = mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java
        )
        return DefaultKafkaProducerFactory(configProps)
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, WebLog> {
        return KafkaTemplate(producerFactory())
    }

}