package com.rabbit.rabbit_mq.global.initdata;

import com.rabbit.rabbit_mq.domain.chat.entity.ChatRoom;
import com.rabbit.rabbit_mq.domain.chat.service.ChatService;
import com.rabbit.rabbit_mq.domain.member.entity.Member;
import com.rabbit.rabbit_mq.domain.member.service.MemberService;
import com.rabbit.rabbit_mq.global.app.AppConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class AllData {
    @Autowired
    @Lazy
    private AllData self;
    private final MemberService memberService;
    private final ChatService chatService;

    @Bean
    @Order(2)
    public ApplicationRunner initAll() {
        return args -> {
            self.work1();
        };
    }

    @Transactional
    public void work1() {
        new File(AppConfig.getTempDirPath()).mkdirs();

        if (memberService.findByUsername("system").isPresent()) return;

        Member memberSystem = memberService.join("system", "1234").getData();
        memberSystem.setRefreshToken("system");

        Member memberAdmin = memberService.join("admin", "1234").getData();
        memberAdmin.setRefreshToken("admin");

        Member memberGarage = memberService.join("garage", "1234").getData();
        memberGarage.setRefreshToken("garage");

        Member memberUser1 = memberService.join("user1", "1234").getData();
        memberUser1.setRefreshToken("user1");

        Member memberUser2 = memberService.join("user2", "1234").getData();
        memberUser2.setRefreshToken("user2");

        Member memberUser3 = memberService.join("user3", "1234").getData();
        memberUser3.setRefreshToken("user3");

        Member memberUser4 = memberService.join("user4", "1234").getData();
        memberUser4.setRefreshToken("user4");

        ChatRoom room1 = chatService.createRoom(memberUser1, "room1");
        ChatRoom room2 = chatService.createRoom(memberUser2, "room2");
        ChatRoom room3 = chatService.createRoom(memberUser3, "room3");

        chatService.writeMessage(room1, memberUser1, "message1");
        chatService.writeMessage(room1, memberUser1, "message2");
        chatService.writeMessage(room1, memberUser1, "message3");

        chatService.writeMessage(room1, memberUser2, "message4");
        chatService.writeMessage(room1, memberUser2, "message5");

        chatService.writeMessage(room1, memberUser3, "message6");

        chatService.writeMessage(room2, memberUser1, "message7");
        chatService.writeMessage(room2, memberUser2, "message8");

        chatService.writeMessage(room3, memberUser1, "message9");

        ChatRoom room4 = chatService.createRoom(memberUser1, "room4");
        ChatRoom room5 = chatService.createRoom(memberUser2, "room5");
        ChatRoom room6 = chatService.createRoom(memberUser3, "room6");

        ChatRoom room7 = chatService.createRoom(memberUser1, "room7");
        ChatRoom room8 = chatService.createRoom(memberUser2, "room8");
        ChatRoom room9 = chatService.createRoom(memberUser3, "room9");

        ChatRoom room10 = chatService.createRoom(memberUser1, "room10");
        ChatRoom room11 = chatService.createRoom(memberUser2, "room11");
        ChatRoom room12 = chatService.createRoom(memberUser3, "room12");

        ChatRoom room13 = chatService.createRoom(memberUser1, "room13");
        ChatRoom room14 = chatService.createRoom(memberUser2, "room14");
        ChatRoom room15 = chatService.createRoom(memberUser3, "room15");

        chatService.writeMessage(room13, memberUser1, "message10");
        chatService.writeMessage(room13, memberUser1, "message11");
        chatService.writeMessage(room13, memberUser1, "message12");

        chatService.writeMessage(room13, memberUser2, "message13");
        chatService.writeMessage(room13, memberUser2, "message14");

        chatService.writeMessage(room13, memberUser3, "message15");

        chatService.writeMessage(room14, memberUser1, "message16");
        chatService.writeMessage(room14, memberUser2, "message17");

        chatService.writeMessage(room15, memberUser1, "message18");

    }
}
