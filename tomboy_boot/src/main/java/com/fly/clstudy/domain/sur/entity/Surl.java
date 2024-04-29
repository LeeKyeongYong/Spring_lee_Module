package com.fly.clstudy.domain.sur.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fly.clstudy.domain.member.entity.Member;
import com.fly.clstudy.global.jpa.entity.BaseTime;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Builder
@Getter
@Setter
public class Surl extends BaseTime {
    @ManyToOne
    @JsonIgnore
    private Member author;
    private String body;
    private String url;
    @Setter(AccessLevel.NONE)
    private long count;

    public void increaseCount() {
        count++;
    }
}