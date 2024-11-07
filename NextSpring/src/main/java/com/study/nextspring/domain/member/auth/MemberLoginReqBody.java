package com.study.nextspring.domain.member.auth;

import jakarta.validation.constraints.NotBlank;

public record MemberLoginReqBody(@NotBlank String username, @NotBlank String password) {
}