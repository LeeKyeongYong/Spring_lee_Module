package com.rabbit.rabbit_mq.domain.chat.controller;

import com.rabbit.rabbit_mq.domain.chat.data.CreateMessageReqBody;
import com.rabbit.rabbit_mq.domain.chat.dto.ChatMessageDto;
import com.rabbit.rabbit_mq.domain.chat.dto.ChatRoomDto;
import com.rabbit.rabbit_mq.domain.chat.entity.ChatMessage;
import com.rabbit.rabbit_mq.domain.chat.entity.ChatRoom;
import com.rabbit.rabbit_mq.domain.chat.service.ChatService;
import com.rabbit.rabbit_mq.domain.member.entity.Member;
import com.rabbit.rabbit_mq.domain.member.service.MemberService;
import com.rabbit.rabbit_mq.global.https.ReqData;
import com.rabbit.rabbit_mq.global.security.entity.SecurityUser;
import com.rabbit.rabbit_mq.global.stomp.template.StompMessageTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
    private final ReqData rq;

    //getAllChatRooms
    @GetMapping("/RoomList")
    public String showChatRooms(Model model) {
        List<ChatRoomDto> chatRooms = chatService.getAllChatRooms();
        model.addAttribute("chatRooms", chatRooms);
        return "domain/chat/chatRoomList"; // chatRoomList.html 템플릿을 반환
    }

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
    public List<ChatMessageDto> getRoomMessages(
            @PathVariable long roomId
    ) {
        List<ChatMessage> messages = chatService.findMessageByRoomId(roomId);

        return messages
                .stream()
                .map(ChatMessageDto::new)
                .toList();
    }

    @MessageMapping("/chat/{roomId}/messages/create")
    @Transactional
    public void createMessage(CreateMessageReqBody createMessageReqBody,
                              @DestinationVariable long roomId,
                              Authentication authentication // 웹 소켓에서는 이런식으로 로그인한 유저정보를 얻을 수 있다.
    ){

        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        Member member = memberService.getReferenceById(securityUser.getId());
        ChatRoom chatRoom = chatService.findRoomById(roomId).get();
        ChatMessage chatMessage = chatService.writeMessage(chatRoom,member, createMessageReqBody.body());
        ChatMessageDto chatMessageDto = new ChatMessageDto(chatMessage);
        template.convertAndSend("topic", "chat" + roomId + "MessageCreated", chatMessageDto);
    }
}
