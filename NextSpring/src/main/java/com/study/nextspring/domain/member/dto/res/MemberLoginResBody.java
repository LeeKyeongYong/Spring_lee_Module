package com.study.nextspring.domain.member.dto.res;

import com.study.nextspring.domain.member.dto.MemberDto;

record MemberLoginResBody(
        MemberDto item,
        String apiKey
) {
}