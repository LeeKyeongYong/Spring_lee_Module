package com.rabbit.rabbit_mq.domain.chat.controller;

import com.rabbit.rabbit_mq.domain.chat.data.CreateMessageReqBody;
import com.rabbit.rabbit_mq.domain.chat.dto.ChatMessageDto;
import com.rabbit.rabbit_mq.domain.chat.entity.ChatMessage;
import com.rabbit.rabbit_mq.domain.chat.entity.ChatRoom;
import com.rabbit.rabbit_mq.domain.chat.service.ChatService;
import com.rabbit.rabbit_mq.domain.member.entity.Member;
import com.rabbit.rabbit_mq.domain.member.service.MemberService;
import com.rabbit.rabbit_mq.global.stomp.StompMessageTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatController {
    private final ChatService chatService;
    private final StompMessageTemplate template;
    private final MemberService memberService;
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{roomId}")
    public String showRoom(
            @PathVariable long roomId,
            Model model
    ) {
        ChatRoom chatRoom = chatService.findRoomById(roomId).get();
        model.addAttribute("chatRoom", chatRoom);

        return "domain/chat/room";
    }

    @GetMapping("/{roomId}/messages")
    @ResponseBody
    public List<ChatMessageDto> showRoomMessages(
            @PathVariable long roomId,
            Model model
    ) {
        List<ChatMessage> messages = chatService.findMessageByRoomId(roomId);

        return messages
                .stream()
                .map(message -> new ChatMessageDto(message.getId(), message.getChatRoom().getId(), message.getWriter().getName(), message.getBody()))
                .toList();
    }

    @MessageMapping("/chat/{roomId}/messages/create")
    @Transactional
    public void createMessage(CreateMessageReqBody createMessageReqBody, @DestinationVariable long roomId){
        Member member = memberService.findByUsername("user1").get();
        ChatRoom chatRoom = chatService.findRoomById(roomId).get();
        ChatMessage chatMessage = chatService.writeMessage(chatRoom,member, createMessageReqBody.body());
        ChatMessageDto chatMessageDto = new ChatMessageDto(chatMessage.getId(),chatMessage.getChatRoom().getId(),chatMessage.getWriter().getName(),chatMessage.getBody());
        template.convertAndSend("topic", "chat" + roomId + "MessageCreated", chatMessage);
    }
}
