package com.study.nextspring.domain.member.dto.req;

import jakarta.validation.constraints.NotBlank;

public record MemberModifyMeReqBody(
        @NotBlank
        String nickname
) {
}