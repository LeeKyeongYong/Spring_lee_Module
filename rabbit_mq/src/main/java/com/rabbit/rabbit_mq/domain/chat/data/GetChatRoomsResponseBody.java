package com.rabbit.rabbit_mq.domain.chat.data;

import com.rabbit.rabbit_mq.domain.chat.dto.ChatRoomDto;
import com.rabbit.rabbit_mq.global.standard.PageDto;
import lombok.NonNull;

public record GetChatRoomsResponseBody(@NonNull PageDto<ChatRoomDto> itemPage) {
}