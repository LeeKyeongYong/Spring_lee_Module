package com.rabbit.rabbit_mq.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatMessageDto {
    private long id;
    private long chatRoomId;
    private String writerName;
    private String body;
}
