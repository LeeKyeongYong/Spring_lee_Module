package com.study.nextspring.domain.member.member.auth;

import com.study.nextspring.domain.member.member.dto.MemberDto;
import lombok.NonNull;

public record MemberLoginResBody(@NonNull MemberDto item) {
}