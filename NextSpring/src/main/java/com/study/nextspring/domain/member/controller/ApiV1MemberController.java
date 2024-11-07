package com.study.nextspring.domain.member.controller;

import com.study.nextspring.domain.member.auth.MeResponseBody;
import com.study.nextspring.domain.member.auth.MemberAuthAndMakeTokensResBody;
import com.study.nextspring.domain.member.auth.MemberLoginReqBody;
import com.study.nextspring.domain.member.auth.MemberLoginResBody;
import com.study.nextspring.domain.member.dto.MemberDto;
import com.study.nextspring.domain.member.service.MemberService;
import com.study.nextspring.global.base.Empty;
import com.study.nextspring.global.httpsdata.ReqData;
import com.study.nextspring.global.httpsdata.RespData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class ApiV1MemberController {
    private final MemberService memberService;
    private ReqData rq;

    @PostMapping(value = "/login")
    public RespData<MemberLoginResBody> login(@Valid @RequestBody MemberLoginReqBody body) {
        RespData<MemberAuthAndMakeTokensResBody> authAndMakeTokensRs = memberService.authAndMakeTokens(
                body.username(),
                body.password()
        );

        rq.setCrossDomainCookie("refreshToken",authAndMakeTokensRs.getData().refreshToken());
        rq.setCrossDomainCookie("accessToken",authAndMakeTokensRs.getData().accessToken());

        return authAndMakeTokensRs.newDataOf(
                new MemberLoginResBody(
                        new MemberDto(authAndMakeTokensRs.getData().member())
                )
        );
    }

    @GetMapping("/me")
    public RespData<MeResponseBody> getMe() {
        return RespData.of(
                new MeResponseBody(
                        new MemberDto(rq.getMember())
                )
        );
    }

    @PostMapping("/logout")
    public RespData<Empty> logout() {
        rq.setLogout();

        return RespData.of("로그아웃 성공");
    }
}