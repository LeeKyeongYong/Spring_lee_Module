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
    fun orderProducerFactory(): ProducerFactory<String, OrderEvent> {
        val configProps = mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java
        )
        return DefaultKafkaProducerFactory(configProps)
    }

    // 기존의 파라미터가 있는 메서드는 제거하고 이 메서드만 남김
    @Bean
    fun orderKafkaTemplate(): KafkaTemplate<String, OrderEvent> {
        return KafkaTemplate(orderProducerFactory()).apply {
            defaultTopic = "order-events"
        }
    }

    @Bean
    fun orderKafkaListenerContainerFactory(
        consumerFactory: ConsumerFactory<String, OrderEvent>
    ): ConcurrentKafkaListenerContainerFactory<String, OrderEvent> {
        return ConcurrentKafkaListenerContainerFactory<String, OrderEvent>().apply {
            this.consumerFactory = consumerFactory
            setConcurrency(3)
            containerProperties.ackMode = ContainerProperties.AckMode.MANUAL_IMMEDIATE
        }
    }

    @Bean
    fun webLogProducerFactory(): ProducerFactory<String, WebLog> {  // 이름 변경
        val configProps = mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java
        )
        return DefaultKafkaProducerFactory(configProps)
    }

    @Bean
    fun webLogKafkaTemplate(): KafkaTemplate<String, WebLog> {  // 이름 변경
        return KafkaTemplate(webLogProducerFactory())
    }
}