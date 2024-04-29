package com.fly.clstudy.domain.member.dto;


import com.fly.clstudy.domain.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class MemberDto {
    private long id;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private String username;
    private String nickname;

    public MemberDto(Member member) {
        this.id = member.getId();
        this.createDate = member.getCreateDate();
        this.modifyDate = member.getModifyDate();
        this.username = member.getUsername();
        this.nickname = member.getNickname();
    }
}