package com.study.nextspring.domain.member.controller;

import com.study.nextspring.domain.member.auth.MeResponseBody;
import com.study.nextspring.domain.member.auth.MemberAuthAndMakeTokensResBody;
import com.study.nextspring.domain.member.auth.MemberLoginReqBody;
import com.study.nextspring.domain.member.auth.MemberLoginResBody;
import com.study.nextspring.domain.member.dto.AccessTokenMemberInfoDto;
import com.study.nextspring.domain.member.dto.MemberDto;
import com.study.nextspring.domain.member.entity.Member;
import com.study.nextspring.domain.member.service.MemberService;
import com.study.nextspring.global.base.Empty;
import com.study.nextspring.global.httpsdata.ReqData;
import com.study.nextspring.global.httpsdata.RespData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

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

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public RespData<MemberLoginResBody> login(
            @RequestBody @Valid MemberLoginReqBody reqBody
    ) {
        Member member = memberService
                .findByUsername(reqBody.username())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "존재하지 않는 사용자입니다."));

        if (!member.matchPassword(reqBody.password()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");

        return RespData.of(
                "200-1",
                "%s님 환영합니다.".formatted(member.getName()),
                new MemberLoginResBody(
                        new MemberDto(member)
                )
        );
    }

    @GetMapping("/me")
    @Operation(summary = "내 정보")
    public RespData<MemberDto> getMe() {
        return RespData.of(
                new MemberDto(rq.getMember())
        );
    }

    @PostMapping("/logout")
    public RespData<Empty> logout() {
        rq.setLogout();
        return RespData.of("로그아웃 성공");
    }
}