package com.study.nextspring.domain.member.auth;

import com.study.nextspring.domain.member.dto.MemberDto;
import lombok.NonNull;

public record MemberLoginResBody(@NonNull MemberDto item) {
}