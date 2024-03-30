package com.rabbit.rabbit_mq.domain.member.data;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestBody(@NotBlank String username, @NotBlank String password) {
}