package com.study.nextspring.domain.member.dto;

import com.study.nextspring.domain.member.entity.Member;
import lombok.Getter;
import org.springframework.lang.NonNull;
import java.time.LocalDateTime;


@Getter
public class MemberDto {

    @NonNull
    private long id;
    @NonNull
    private LocalDateTime createDate;
    @NonNull
    private LocalDateTime modifyDate;
    @NonNull
    private String nickname;

    public MemberDto(Member member) {
        this.id = member.getId();
        this.createDate = member.getCreateDate();
        this.modifyDate = member.getModifyDate();
        this.nickname = member.getNickname();
    }
}