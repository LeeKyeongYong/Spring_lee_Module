package com.surl.studyurl.domain.member.dto;

import com.surl.studyurl.domain.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

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
    private String name;
    @NonNull
    private List<String> authorities;

    public MemberDto(Member member) {
        this.id = member.getId();
        this.createDate = member.getCreateDate();
        this.modifyDate = member.getModifyDate();
        this.name = member.getName();
        this.authorities = member.getAuthoritiesAsStringList();
    }
}
