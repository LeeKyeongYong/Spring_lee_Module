package com.rabbit.rabbit_mq.domain.chat.service;


import com.rabbit.rabbit_mq.domain.chat.entity.ChatMessage;
import com.rabbit.rabbit_mq.domain.chat.entity.ChatRoom;
import com.rabbit.rabbit_mq.domain.chat.repository.ChatMessageRepository;
import com.rabbit.rabbit_mq.domain.chat.repository.ChatRoomRepository;
import com.rabbit.rabbit_mq.domain.member.entity.Member;
import com.rabbit.rabbit_mq.global.standard.KwTypeV2;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    public Optional<ChatRoom> findRoomById(long roomId){
        return chatRoomRepository.findById(roomId);
    }

    @Transactional
    public ChatRoom createRoom(Member owner, String name){
        ChatRoom chatRoom = ChatRoom
                .builder()
                .owner(owner)
                .name(name)
                .published(true)
                .build();

        return chatRoomRepository.save(chatRoom);
    }

    @Transactional
    public ChatMessage writeMessage(ChatRoom room, Member writer,String body){
        ChatMessage chatMessage = ChatMessage
                .builder()
                .chatRoom(room)
                .writer(writer)
                .body(body)
                .build();

        return chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessage> findMessageByRoomId(long roomId){
        return chatMessageRepository.findByChatRoomId(roomId);
    }
    public int count() {
        return (int) chatRoomRepository.count();
    }

    public Page<ChatRoom> findRoomByKw(KwTypeV2 kwType, String kw, Member owner, Boolean published, Pageable pageable) {
        return chatRoomRepository.findByKw(kwType, kw, owner, published, pageable);
    }
}
