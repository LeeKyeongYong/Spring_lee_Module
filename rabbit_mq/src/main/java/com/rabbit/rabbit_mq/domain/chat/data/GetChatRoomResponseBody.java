package com.rabbit.rabbit_mq.domain.chat.data;

import com.rabbit.rabbit_mq.domain.chat.dto.ChatRoomDto;
import lombok.NonNull;

public record GetChatRoomResponseBody(@NonNull ChatRoomDto item) {
}
