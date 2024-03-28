package com.rabbit.rabbit_mq.domain.chat.repository;

import com.rabbit.rabbit_mq.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {
    List<ChatMessage> findByChangeRoomId(long roomId);
}
