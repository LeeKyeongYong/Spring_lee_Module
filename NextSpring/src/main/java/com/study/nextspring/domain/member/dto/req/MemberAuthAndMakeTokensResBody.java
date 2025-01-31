package com.study.nextspring.domain.member.dto.req;

import com.study.nextspring.domain.member.entity.Member;

public record MemberAuthAndMakeTokensResBody(
        Member member,
        String accessToken,
        String refreshToken
) {
}