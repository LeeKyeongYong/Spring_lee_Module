package com.study.nextspring.domain.member.dto.req;

import jakarta.validation.constraints.NotBlank;

public record MemberLoginReqBody(
        @NotBlank
        String username,
        @NotBlank
        String password
    ) {
}