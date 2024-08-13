package com.study.mstudy.item.controller;

import com.study.mstudy.item.dto.ItemDTO;
import com.study.mstudy.item.dto.ResponseDTO;
import com.study.mstudy.item.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="v1/item")
@Slf4j
@RequiredArgsConstructor
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping(value="/add",method= RequestMethod.POST)
    public ResponseEntity<ResponseDTO> add(@Valid @RequestBody ItemDTO itemDTO){

        ResponseDTO.ResponseDTOBuilder responseDTOBuilder = ResponseDTO.builder();

        itemService.insertItem(itemDTO);
        log.debug("request add item id= {}",itemDTO.getId());

        responseDTOBuilder.code("200").message("success");
        return ResponseEntity.ok(responseDTOBuilder.build());
    }

}
