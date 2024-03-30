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
@Getter
@Setter
public class ChatRoom extends BaseTime {
    @ManyToOne
    private Member owner;
    private String name;
    private boolean published;
}