package com.surl.studyurl.domain.member.controller;

import com.surl.studyurl.domain.member.dto.MemberDto;
import com.surl.studyurl.domain.member.record.LoginRequestBody;
import com.surl.studyurl.domain.member.record.LoginResponseBody;
import com.surl.studyurl.domain.member.service.MemberService;
import com.surl.studyurl.global.httpsdata.ReqData;
import com.surl.studyurl.global.httpsdata.RespData;
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
    private final ReqData reqData;

    @PostMapping(value = "/login")
    public RespData<LoginResponseBody> login(@Valid @RequestBody LoginRequestBody body){
        RespData<MemberService.AuthAndMakeTokensResponseBody> authAndMakeTokensRs = memberService.authAndMakeTokens(
                body.userid,
                body.password
        );

        reqData.setCrossDomainCookie("refreshToken", authAndMakeTokensRs.getData().refreshToken());
        reqData.setCrossDomainCookie("accessToken", authAndMakeTokensRs.getData().accessToken());

        return authAndMakeTokensRs.newDataOf(
                new LoginResponseBody(
                        new MemberDto(authAndMakeTokensRs.getData().member())
                )
        );
    }

}
