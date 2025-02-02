package com.study.nextspring.domain.member.controller;


import com.study.nextspring.domain.auth.service.AuthTokenService;
import com.study.nextspring.domain.member.dto.MemberDto;
import com.study.nextspring.domain.member.dto.req.MemberLoginReqBody;
import com.study.nextspring.domain.member.dto.res.MemberLoginResBody;
import com.study.nextspring.domain.member.entity.Member;
import com.study.nextspring.domain.member.service.MemberService;
import com.study.nextspring.global.exception.ServiceException;
import com.study.nextspring.global.httpsdata.ReqData;
import com.study.nextspring.global.httpsdata.RespData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
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
    private final AuthTokenService authTokenService;

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public RespData<MemberLoginResBody> login(
            HttpServletResponse resp,
            @RequestBody @Valid MemberLoginReqBody reqBody
    ) {
        // 사용자 조회
        Member member = memberService
                .findByUsername(reqBody.username())
                .orElseThrow(() -> new ServiceException("401-1", "존재하지 않는 사용자입니다."));

        // 비밀번호 확인
        if (!memberService.validatePassword(member, reqBody.password()))
            throw new ServiceException("401-2", "비밀번호가 일치하지 않습니다.");

        // JWT 토큰 생성
        String accessToken = authTokenService.genAccessToken(member);

        // 쿠키에 토큰과 API 키 설정
        rq.setCookie("accessToken", accessToken);
        rq.setCookie("apiKey", member.getApiKey());

        return RespData.of(
                "200-1",
                "%s님 환영합니다.".formatted(member.getName()),
                new MemberLoginResBody(
                        new MemberDto(member),
                        member.getApiKey(),
                        accessToken
                )
        );
    }

    @GetMapping("/me")
    @Operation(summary = "내 정보")
    @Transactional(readOnly = true)
    public MemberDto getMe() {
        Member actor = rq.findByActor().get();
        return new MemberDto(actor);
    }

    @DeleteMapping("/logout")
    @Transactional(readOnly = true)
    public RespData<Void> logout() {
        rq.deleteCookie("accessToken");
        rq.deleteCookie("apiKey");

        return new RespData<>(
                "200-1",
                "로그아웃 되었습니다."
        );
    }
}
