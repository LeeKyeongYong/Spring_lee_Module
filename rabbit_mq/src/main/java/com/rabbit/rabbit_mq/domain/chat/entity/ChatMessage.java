package com.rabbit.rabbit_mq.domain.chat.entity;


import com.rabbit.rabbit_mq.domain.member.entity.Member;
import com.rabbit.rabbit_mq.global.jpa.BaseTime;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Setter
@Getter
public class ChatMessage extends BaseTime {
    @ManyToOne
    private ChatRoom chatRoom;
    @ManyToOne
    private Member writer;
    private String body;
}
