package com.surl.studyurl.domain.member.record;

import com.surl.studyurl.domain.member.dto.MemberDto;
import lombok.NonNull;

public record LoginResponseBody(@NonNull MemberDto item) {
}