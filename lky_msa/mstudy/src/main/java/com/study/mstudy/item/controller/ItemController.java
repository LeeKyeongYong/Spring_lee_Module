package com.study.mstudy.item.controller;


import com.study.mstudy.item.dto.ItemDTO;
import com.study.mstudy.item.dto.ResponseDTO;
import com.study.mstudy.item.exception.ApiException;
import com.study.mstudy.item.service.ItemService;
import com.study.mstudy.item.valid.ItemTypeValid;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@OpenAPIDefinition(info = @Info(title="물품 처리요청 API",description = "물품 처리 요청 api", version = "v1.0"))
@RestController
@RequestMapping(value="v1/item")
@Slf4j
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemService itemService;

    @Operation(summary = "물품등록 요청", description = "물품 등록을 진행할 수 있다.", tags = { "addItem" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS"),
            @ApiResponse(responseCode = "501", description = "API EXCEPTION")
    })
    @RequestMapping(value="/add/{itemType}", method=RequestMethod.POST)
    public ResponseEntity<ResponseDTO> add(
            HttpServletRequest req,
            @Valid @RequestBody ItemDTO itemDTO,@ItemTypeValid @PathVariable String itemType) throws Exception{
        ResponseDTO.ResponseDTOBuilder responseBuilder = ResponseDTO.builder();

        String accountId = req.getHeader("accountId").toString().replace("[", "").replace("]", "");
        log.info("accountId = {}", accountId);

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

        //임의로만든 test
        try{
            Integer.parseInt("test");
        }catch(Exception e){
            throw new ApiException("test에러 입니다.");
        }
        */

        itemDTO.setItemType(itemType);
        itemService.insertItem(itemDTO, accountId);
        log.debug("request add item id = {}", itemDTO.getId());

        responseBuilder.code("200").message("success");
        return ResponseEntity.ok(responseBuilder.build());
    }
}
