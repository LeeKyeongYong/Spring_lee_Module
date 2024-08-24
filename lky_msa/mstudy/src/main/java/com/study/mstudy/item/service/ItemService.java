package com.study.mstudy.item.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.mstudy.global.feign.HistoryFeignClient;
import com.study.mstudy.item.domain.Item;
import com.study.mstudy.item.dto.ItemDTO;
import com.study.mstudy.item.repository.ItemRepository;
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

    @Value(value = "${topic.name}")
    private String topicName;

    public void insertItem(ItemDTO itemDTO,String accountId) {
        SimpleDateFormat form = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = form.format(new Date());

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
        log.info("feign result = {}", historyFeignClient.saveHistory(historyMap));
        
        //rest통신
        log.info("resttemplate result = {}", restTemplate.postForObject("http://HISTORY-SERVICE/v1/history/save", historyMap, String.class));

        try {
           // jmsTemplate.convertAndSend(activeMq, objectMapper.writeValueAsString(itemDTO));
            this.kafkaTemplate.send(topicName, objectMapper.writeValueAsString(itemDTO));
        } catch (JmsException | JsonProcessingException e) {
            e.printStackTrace();
        }

    }
}
