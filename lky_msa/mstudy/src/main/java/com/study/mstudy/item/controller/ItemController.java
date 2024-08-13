package com.study.mstudy.item.controller;


import com.study.mstudy.item.dto.ItemDTO;
import com.study.mstudy.item.dto.ResponseDTO;
import com.study.mstudy.item.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value="v1/item")
@Slf4j
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @RequestMapping(value="/add", method=RequestMethod.POST)
    public ResponseEntity<ResponseDTO> add(@Valid @RequestBody ItemDTO itemDTO){
        ResponseDTO.ResponseDTOBuilder responseBuilder = ResponseDTO.builder();

        itemService.insertItem(itemDTO);
        log.debug("request add item id = {}", itemDTO.getId());

        responseBuilder.code("200").message("success");
        return ResponseEntity.ok(responseBuilder.build());
    }
}
