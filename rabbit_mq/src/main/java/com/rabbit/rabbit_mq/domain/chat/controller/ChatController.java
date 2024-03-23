package com.rabbit.rabbit_mq.domain.chat.controller;

import com.rabbit.rabbit_mq.domain.chat.entity.ChatMessage;
import com.rabbit.rabbit_mq.domain.chat.entity.ChatRoom;
import com.rabbit.rabbit_mq.domain.chat.service.ChatService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @GetMapping("/{roomId}")
    public String showRoom(
            @PathVariable long roomId,
            Model model
    ) {
        ChatRoom chatRoom = chatService.findRoomById(roomId).get();
        model.addAttribute("chatRoom", chatRoom);

        return "domain/chat/chat/room";
    }

    @GetMapping("/{roomId}/messages")
    @ResponseBody
    public List<ChatMessage> showRoomMessages(
            @PathVariable long roomId,
            Model model
    ) {
        List<ChatMessage> messages = chatService.findMessagesByRoomId(roomId);

        return messages;
    }
}
