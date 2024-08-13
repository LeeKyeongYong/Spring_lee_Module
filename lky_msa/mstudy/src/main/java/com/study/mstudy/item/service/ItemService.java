package com.study.mstudy.item.service;

import com.study.mstudy.item.domain.Item;
import com.study.mstudy.item.dto.ItemDTO;
import com.study.mstudy.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;


    public void insertItem(ItemDTO itemDTO) {
        SimpleDateFormat form = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = form.format(new Date());

        Item item = Item.builder()
                .id(itemDTO.getId())
                .name(itemDTO.getName())
                .description(itemDTO.getDescription())
                .count(itemDTO.getCount())
                .regDts(date)
                .updDts(date)
                .build();
        itemRepository.save(item);

    }
}
