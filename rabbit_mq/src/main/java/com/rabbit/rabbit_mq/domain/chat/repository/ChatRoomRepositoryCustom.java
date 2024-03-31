package com.rabbit.rabbit_mq.domain.chat.repository;

import com.rabbit.rabbit_mq.domain.chat.entity.ChatRoom;
import com.rabbit.rabbit_mq.domain.member.entity.Member;
import com.rabbit.rabbit_mq.global.standard.enums.KwTypeV2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatRoomRepositoryCustom {
    Page<ChatRoom> findByKw(KwTypeV2 kwType, String kw, Member owner, Boolean published, Pageable pageable);
}