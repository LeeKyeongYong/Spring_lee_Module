package com.rabbit.rabbit_mq.domain.chat.dto;

import com.rabbit.rabbit_mq.domain.chat.entity.ChatMessage;
import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Data
@NoArgsConstructor(access = PROTECTED)
public class ChatMessageDto {
    private long id;
    private long chatRoomId;
    private String writerName;
    private String body;
    private long writerId;

    public ChatMessageDto(ChatMessage message) {
        this.id = message.getId();
        this.chatRoomId = message.getChatRoom().getId();
        this.writerName = message.getWriter().getName();
        this.body = message.getBody();
        this.writerId = message.getWriter().getId();
    }
}
