package com.rabbit.rabbit_mq.domain.chat.data;

import com.rabbit.rabbit_mq.domain.chat.dto.ChatMessageDto;
import lombok.NonNull;

import java.util.List;

public record GetChatMessagesResponseBody(@NonNull List<ChatMessageDto> items) {
}