package com.krstudy.kapi.domain.item.service

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.krstudy.kapi.domain.item.entity.Item
import com.krstudy.kapi.domain.item.entity.ItemDTO
import com.krstudy.kapi.domain.item.repository.ItemRepository
import com.krstudy.kapi.global.app.HistoryFeignClient
import io.github.resilience4j.bulkhead.annotation.Bulkhead
import io.github.resilience4j.retry.annotation.Retry
import lombok.RequiredArgsConstructor
import org.slf4j.LoggerFactory
import org.springframework.jms.JmsException
import org.springframework.jms.core.JmsTemplate
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import jakarta.jms.Queue
import org.springframework.beans.factory.annotation.Value
@Service
@RequiredArgsConstructor
class ItemService(
    private val itemRepository: ItemRepository,
    private val historyFeignClient: HistoryFeignClient,
    private val restTemplate: RestTemplate,
    private val jmsTemplate: JmsTemplate,
    private val activeMq: Queue,
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val itemThreadExecutor: Executor
) {

    @Value("\${topic.name}")
    lateinit var topicName: String

    private val objectMapper = ObjectMapper()
    private val log = LoggerFactory.getLogger(ItemService::class.java) // SLF4J logger

    @Bulkhead(name = "bulkInsertItem", fallbackMethod = "bulkheadFallback")
    @Retry(name = "insertItem", fallbackMethod = "fallback")
    fun insertItem(itemDTO: ItemDTO, accountId: String) {
        val date = SimpleDateFormat("yyyyMMddHHmmss").format(Date())

        CompletableFuture.runAsync({
            try {
                Thread.sleep(5000L)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            // Item 객체를 생성하는 방법 수정
            val item = Item(
                id = itemDTO.id,
                name = itemDTO.name,
                description = itemDTO.description,
                count = itemDTO.count,
                regDts = date,
                itemType = itemDTO.itemType,
                updDts = date,
                accountId = accountId
            )

            itemRepository.save(item)

            val historyMap = mutableMapOf<String, Any>(
                "accountId" to accountId,
                "itemId" to itemDTO.id
            )

            try {
                jmsTemplate.convertAndSend(activeMq, objectMapper.writeValueAsString(itemDTO))
                // this.kafkaTemplate.send(topicName, objectMapper.writeValueAsString(itemDTO))
            } catch (e: JsonProcessingException) {
                e.printStackTrace()
            }
        }, itemThreadExecutor)
    }


    private fun fallback(e: Throwable) {
        log.error("fallback check.", e)
    }

    private fun bulkheadFallback(e: Throwable) {
        log.error("bulk fallback check.", e)
    }
}