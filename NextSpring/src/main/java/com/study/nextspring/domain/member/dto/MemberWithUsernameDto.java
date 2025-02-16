package com.study.nextspring.domain.member.dto;

import com.study.nextspring.domain.member.entity.Member;
import lombok.Getter;
import org.springframework.lang.NonNull;


@Getter
public class MemberWithUsernameDto extends MemberDto {
    @NonNull
    private final String username;

    public MemberWithUsernameDto(Member member) {
        super(member);
        this.username = member.getUsername();
    }
}