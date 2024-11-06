package com.study.nextspring.domain.member.member.auth;

import com.study.nextspring.domain.member.member.entity.Member;

public record MemberAuthAndMakeTokensResBody(
        Member member,
        String accessToken,
        String refreshToken
) {
}