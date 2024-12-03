package com.study.nextspring.domain.member.dto;

import com.study.nextspring.domain.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class MemberDto {
    @NonNull
    private long id;
    @NonNull
    private LocalDateTime createDate;
    @NonNull
    private LocalDateTime modifyDate;
    @NonNull
    private String name; // nickname
    @NonNull
    private String profileImgUrl; // refreshToken
    @NonNull
    private List<String> authorities;
    @NonNull
    private String nickname;
    @NonNull
    private boolean social;

    public MemberDto(Member member) {
        this.id = member.getId();
        this.createDate = member.getCreateDate();
        this.modifyDate = member.getModifyDate();
        this.name = member.getName(); // nickname
        this.nickname = member.getNickname();
        this.profileImgUrl = member.getProfileImgUrlOrDefault(); // profileImgUrl
        this.authorities = member.getAuthoritiesAsStringList();
        this.social = member.isSocial();
    }



}