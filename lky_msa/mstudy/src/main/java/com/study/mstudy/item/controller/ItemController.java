package com.study.mstudy.item.controller;


import com.study.mstudy.item.dto.ItemDTO;
import com.study.mstudy.item.dto.ResponseDTO;
import com.study.mstudy.item.exception.ApiException;
import com.study.mstudy.item.service.ItemService;
import com.study.mstudy.item.valid.ItemTypeValid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value="v1/item")
@Slf4j
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemService itemService;

    @RequestMapping(value="/add/{itemType}", method=RequestMethod.POST)
    public ResponseEntity<ResponseDTO> add(@Valid @RequestBody ItemDTO itemDTO,@ItemTypeValid @PathVariable String itemType) throws Exception{
        ResponseDTO.ResponseDTOBuilder responseBuilder = ResponseDTO.builder();

        /*
        log.debug("path.variable itemType = {}", itemType);
        boolean hasItemType = false;
        ItemType [] itemTypeList = ItemType.values();
        for(ItemType i : itemTypeList) {
            hasItemType = i.hasItemCd(itemType);
            if(hasItemType) break;
        }

        if(!hasItemType) {
            responseBuilder.code("500").message("invalid itemType .[" + itemType + "]");
            return ResponseEntity.ok(responseBuilder.build());
        }else {
            itemDTO.setItemType(itemType);
        }

       */

        try{
            Integer.parseInt("test");
        }catch(Exception e){
            throw new ApiException("test에러 입니다.");
        }

        itemDTO.setItemType(itemType);
        itemService.insertItem(itemDTO);
        log.debug("request add item id = {}", itemDTO.getId());

        responseBuilder.code("200").message("success");
        return ResponseEntity.ok(responseBuilder.build());
    }
}
