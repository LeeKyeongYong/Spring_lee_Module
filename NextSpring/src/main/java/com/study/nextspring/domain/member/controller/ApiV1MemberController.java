package com.study.nextspring.domain.member.controller;

import com.study.nextspring.domain.member.auth.MeResponseBody;
import com.study.nextspring.domain.member.auth.MemberAuthAndMakeTokensResBody;
import com.study.nextspring.domain.member.auth.MemberLoginReqBody;
import com.study.nextspring.domain.member.auth.MemberLoginResBody;
import com.study.nextspring.domain.member.dto.MemberDto;
import com.study.nextspring.domain.member.entity.Member;
import com.study.nextspring.domain.member.service.MemberService;
import com.study.nextspring.global.base.Empty;
import com.study.nextspring.global.httpsdata.ReqData;
import com.study.nextspring.global.httpsdata.RespData;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Transactional(readOnly = true)
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "ApiV1MemberController", description = "MEMBER API 컨트롤러")
public class ApiV1MemberController {
    private static final Logger log = LoggerFactory.getLogger(ApiV1MemberController.class);
    private final MemberService memberService;
    @Autowired
    private ReqData rq;

    @PostMapping(value = "/login")
    public RespData<MemberLoginResBody> login(@Valid @RequestBody MemberLoginReqBody body) {
        RespData<MemberAuthAndMakeTokensResBody> authAndMakeTokensRs = memberService.authAndMakeTokens(
                body.username(),
                body.password()
        );

        rq.setCrossDomainCookie("refreshToken", authAndMakeTokensRs.getData().refreshToken());
        rq.setCrossDomainCookie("accessToken", authAndMakeTokensRs.getData().accessToken());

        // Member 엔티티를 MemberDto로 변환
        MemberDto memberDto = new MemberDto(authAndMakeTokensRs.getData().member());

        return authAndMakeTokensRs.newDataOf(
                new MemberLoginResBody(memberDto)
        );
    }

    @GetMapping("/me")
    public RespData<MeResponseBody> getMe() {
        try {
            Member member = rq.getMember();
            if (member == null) {
                return RespData.of("로그인이 필요합니다.", null);
            }

            MemberDto memberDto = member.toDto();
            return RespData.of(new MeResponseBody(memberDto));
        } catch (Exception e) {
            // 로그 추가
            log.error("Error in getMe: ", e);
            return RespData.of("서버 오류가 발생했습니다.", null);
        }
    }

    @PostMapping("/logout")
    public RespData<Empty> logout() {
        rq.setLogout();
        return RespData.of("로그아웃 성공");
    }
}
