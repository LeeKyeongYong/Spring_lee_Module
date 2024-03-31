package com.rabbit.rabbit_mq.domain.chat.controller;

import com.rabbit.rabbit_mq.domain.chat.data.GetChatRoomResponseBody;
import com.rabbit.rabbit_mq.domain.chat.data.GetChatRoomsResponseBody;
import com.rabbit.rabbit_mq.domain.chat.dto.ChatRoomDto;
import com.rabbit.rabbit_mq.domain.chat.entity.ChatRoom;
import com.rabbit.rabbit_mq.domain.chat.service.ChatService;
import com.rabbit.rabbit_mq.global.app.AppConfig;
import com.rabbit.rabbit_mq.global.https.RespData;
import com.rabbit.rabbit_mq.global.standard.enums.KwTypeV2;
import com.rabbit.rabbit_mq.global.standard.dto.PageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/chatRooms")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApiV1ChatRoomController {
    private final ChatService chatService;
    @GetMapping("")
    public RespData<GetChatRoomsResponseBody> getChatRooms(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String kw,
            @RequestParam(defaultValue = "ALL") KwTypeV2 kwType
    ) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, AppConfig.getBasePageSize(), Sort.by(sorts));
        Page<ChatRoom> itemPage = chatService.findRoomByKw(kwType, kw, null, true, pageable);

        Page<ChatRoomDto> _itemPage = itemPage.map(ChatRoomDto::new);

        return RespData.of(
                new GetChatRoomsResponseBody(
                        new PageDto<>(_itemPage)
                )
        );
    }
    @GetMapping("/{id}")
    public RespData<GetChatRoomResponseBody> getChatRoom(
            @PathVariable long id
    ) {
        ChatRoom chatRoom = chatService.findRoomById(id).get();

        return RespData.of(
                new GetChatRoomResponseBody(
                        new ChatRoomDto(chatRoom)
                )
        );
    }
}
