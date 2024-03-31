package com.rabbit.rabbit_mq.domain.chat.controller;

import com.rabbit.rabbit_mq.domain.chat.data.GetChatMessagesResponseBody;
import com.rabbit.rabbit_mq.domain.chat.dto.ChatMessageDto;
import com.rabbit.rabbit_mq.domain.chat.entity.ChatRoom;
import com.rabbit.rabbit_mq.domain.chat.service.ChatService;
import com.rabbit.rabbit_mq.global.https.RespData;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chatMessages")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApiV1ChatMessageController {
    private final ChatService chatService;
    @GetMapping("/{roomId}")
    public RespData<GetChatMessagesResponseBody> getChatMessages(
            @PathVariable long roomId
    ) {
        ChatRoom chatRoom = chatService.findRoomById(roomId).get();
        List<ChatMessageDto> items = chatService.findMessageByRoomId(roomId)
                .stream()
                .map(ChatMessageDto::new)
                .toList();

        return RespData.of(
                new GetChatMessagesResponseBody(items)
        );
    }
}
