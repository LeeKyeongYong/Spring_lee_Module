package com.rabbit.rabbit_mq.domain.chat.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private ChatRoom chatRoom;
    private String writerName;
    private String body;
}
