package com.rabbit.rabbit_mq.domain.member.data;

public record LoginRequestBody(@NotBlank String username, @NotBlank String password) {
}