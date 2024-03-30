package com.rabbit.rabbit_mq.domain.chat.repository;

import com.rabbit.rabbit_mq.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> , ChatRoomRepositoryCustom{
}
