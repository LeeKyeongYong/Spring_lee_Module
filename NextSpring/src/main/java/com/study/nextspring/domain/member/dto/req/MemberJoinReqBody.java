package com.study.nextspring.domain.member.dto.req;

import jakarta.validation.constraints.NotBlank;
public record MemberJoinReqBody(
        @NotBlank
        String username,
        @NotBlank
        String password,
        @NotBlank
        String nickname
        ) {
}