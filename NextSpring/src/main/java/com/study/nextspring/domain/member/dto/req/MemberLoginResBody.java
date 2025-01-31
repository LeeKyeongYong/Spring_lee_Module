package com.study.nextspring.domain.member.dto.req;

import com.study.nextspring.domain.member.dto.MemberDto;
import lombok.NonNull;

public record MemberLoginResBody(@NonNull MemberDto item) {
}