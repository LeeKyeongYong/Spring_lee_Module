package com.rabbit.rabbit_mq.domain.member.controller;

import com.rabbit.rabbit_mq.domain.member.data.LoginRequestBody;
import com.rabbit.rabbit_mq.domain.member.data.LoginResponseBody;
import com.rabbit.rabbit_mq.domain.member.dto.MemberDto;
import com.rabbit.rabbit_mq.domain.member.service.MemberService;
import com.rabbit.rabbit_mq.global.https.ReqData;
import com.rabbit.rabbit_mq.global.https.RespData;
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
    private final ReqData rq;
    @PostMapping(value = "/login")
    public RespData<LoginResponseBody> login(@Valid @RequestBody LoginRequestBody body) {
        RespData<MemberService.AuthAndMakeTokensResponseBody> authAndMakeTokensRs = memberService.authAndMakeTokens(
                body.username(),
                body.password()
        );

        rq.setCrossDomainCookie("refreshToken", authAndMakeTokensRs.getData().refreshToken());
        rq.setCrossDomainCookie("accessToken", authAndMakeTokensRs.getData().accessToken());

        return authAndMakeTokensRs.newDataOf(
                new LoginResponseBody(
                        new MemberDto(authAndMakeTokensRs.getData().member())
                )
        );
    }

}
