package com.rabbit.rabbit_mq.domain.member.entity;

import com.rabbit.rabbit_mq.global.jpa.BaseTime;
import jakarta.persistence.Entity;
import lombok.*;
import static lombok.AccessLevel.PROTECTED;
@Entity
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Builder
@Getter
@Setter
public class Member extends BaseTime {

}
