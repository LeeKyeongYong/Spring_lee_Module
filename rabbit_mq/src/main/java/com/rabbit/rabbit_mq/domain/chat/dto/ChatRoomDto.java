package com.rabbit.rabbit_mq.domain.chat.dto;

import com.rabbit.rabbit_mq.domain.chat.entity.ChatRoom;
import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Data
@NoArgsConstructor(access = PROTECTED)
public class ChatRoomDto {
    private long id;
    private String ownerName;
    private String name;

    public ChatRoomDto(ChatRoom room) {
        this.id = room.getId();
        this.ownerName = room.getOwner().getName();
        this.name = room.getName();
    }
}
