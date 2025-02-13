package com.study.nextspring.domain.member.dto.res;

import com.study.nextspring.domain.member.dto.MemberDto;
import lombok.NonNull;

public record MemberLoginResBody(@NonNull MemberDto item,
                                 @NonNull
                                 String apiKey,
                                 @NonNull
                                 String accessToken) {
}