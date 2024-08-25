package com.study.mstudy.item.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.mstudy.global.feign.HistoryFeignClient;
import com.study.mstudy.item.domain.Item;
import com.study.mstudy.item.dto.ItemDTO;
import com.study.mstudy.item.repository.ItemRepository;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import jakarta.jms.Queue;
import org.springframework.beans.factory.annotation.Value;
@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final HistoryFeignClient historyFeignClient;
    private final RestTemplate restTemplate;
    private final JmsTemplate jmsTemplate;
    private final Queue activeMq;
    private final KafkaTemplate<String, String> kafkaTemplate;
    ObjectMapper objectMapper = new ObjectMapper();
    private final Executor itemThreadExecutor;

    @Value(value = "${topic.name}")
    private String topicName;

    @Bulkhead(name = "bulkInsertItem", fallbackMethod = "bulkheadFallback")
    @Retry(name = "insertItem", fallbackMethod = "fallback")
    //@CircuitBreaker(name="itemCircuitBreaker",fallbackMethod = "fallback")
    public void insertItem(ItemDTO itemDTO,String accountId) {
        SimpleDateFormat form = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = form.format(new Date());

        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

        Item item = Item.builder()
                .id(itemDTO.getId())
                .name(itemDTO.getName())
                .description(itemDTO.getDescription())
                .count(itemDTO.getCount())
                .regDts(date)
                .itemType(itemDTO.getItemType())
                .updDts(date)
                .accountId(accountId)
                .build();
        itemRepository.save(item);

        Map<String, Object> historyMap = new HashMap<String, Object>();
        historyMap.put("accountId", accountId);
        historyMap.put("itemId", itemDTO.getId());
        //http통신
        //log.info("feign result = {}", historyFeignClient.saveHistory(historyMap));
        
        //rest통신
        //log.info("resttemplate result = {}", restTemplate.postForObject("http://HISTORY-SERVICE/v1/history/save", historyMap, String.class));

        try {

          jmsTemplate.convertAndSend(activeMq, objectMapper.writeValueAsString(itemDTO));
           // this.kafkaTemplate.send(topicName, objectMapper.writeValueAsString(itemDTO));
        //} catch (JmsException | JsonProcessingException e) {
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }, itemThreadExecutor);

    }

    private void fallback(Throwable e) {
        log.error("fallback check.");
    }

    private void bulkheadFallback(Throwable e) {
        log.error("bulk fallback check.");
    }
}
