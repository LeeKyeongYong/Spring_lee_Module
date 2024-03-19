package com.surl.studyurl.domain.member.record;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestBody(@NotBlank String userid, @NotBlank String password) {
}
