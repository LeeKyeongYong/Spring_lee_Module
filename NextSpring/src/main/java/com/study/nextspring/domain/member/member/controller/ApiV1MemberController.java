package com.study.nextspring.domain.member.member.controller;

import com.study.nextspring.domain.member.member.auth.MemberAuthAndMakeTokensResBody;
import com.study.nextspring.domain.member.member.auth.MemberLoginReqBody;
import com.study.nextspring.domain.member.member.auth.MemberLoginResBody;
import com.study.nextspring.domain.member.member.dto.MemberDto;
import com.study.nextspring.domain.member.member.service.MemberService;
import com.study.nextspring.global.httpsdata.RespData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class ApiV1MemberController {
    private final MemberService memberService;

    @PostMapping(value = "/login")
    public RespData<MemberLoginResBody> login(@Valid @RequestBody MemberLoginReqBody body) {
        RespData<MemberAuthAndMakeTokensResBody> authAndMakeTokensRs = memberService.authAndMakeTokens(
                body.username(),
                body.password()
        );

        return authAndMakeTokensRs.newDataOf(
                new MemberLoginResBody(
                        new MemberDto(authAndMakeTokensRs.getData().member())
                )
        );
    }
}