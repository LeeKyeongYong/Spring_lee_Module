package com.rabbit.rabbit_mq.domain.member.data;

import com.rabbit.rabbit_mq.domain.member.dto.MemberDto;
import lombok.NonNull;

public record LoginResponseBody(@NonNull MemberDto item) {
}