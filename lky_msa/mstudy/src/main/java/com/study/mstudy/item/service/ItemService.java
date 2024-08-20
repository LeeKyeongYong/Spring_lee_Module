package com.study.mstudy.item.service;

import com.study.mstudy.feign.HistoryFeignClient;
import com.study.mstudy.item.domain.Item;
import com.study.mstudy.item.dto.ItemDTO;
import com.study.mstudy.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final HistoryFeignClient historyFeignClient;


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
        log.info("feign result = {}", historyFeignClient.saveHistory(historyMap));

    }
}
