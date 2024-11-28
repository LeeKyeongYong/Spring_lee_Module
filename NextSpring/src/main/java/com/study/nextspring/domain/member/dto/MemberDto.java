package com.study.nextspring.domain.member.dto;

import com.study.nextspring.domain.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
public class MemberDto {
    private long id;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private String username; // nickname
    private String profileImgUrl; // refreshToken
    private List<String> authorities;
    private String nickname;

    private boolean social;

    public MemberDto(Member member) {
        this.id = member.getId();
        this.createDate = member.getCreateDate();
        this.modifyDate = member.getModifyDate();
        this.username = member.getUsername(); // nickname
        this.nickname = member.getNickname();
        this.profileImgUrl = member.getProfileImgUrlOrDefault(); // profileImgUrl
        this.authorities = member.getAuthoritiesAsStringList();
        this.social = member.isSocial();
    }



}