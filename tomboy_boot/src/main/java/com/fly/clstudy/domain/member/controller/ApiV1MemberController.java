package com.fly.clstudy.domain.member.controller;


import com.fly.clstudy.domain.member.data.MemberLoginReqBody;
import com.fly.clstudy.domain.member.data.MemberLoginRespBody;
import com.fly.clstudy.domain.member.dto.MemberDto;
import com.fly.clstudy.domain.member.entity.Member;
import com.fly.clstudy.domain.member.service.MemberService;
import com.fly.clstudy.domain.member.data.MemberJoinReqBody;
import com.fly.clstudy.global.exceptions.GlobalException;
import com.fly.clstudy.global.https.ReqData;
import com.fly.clstudy.global.https.RespData;
import com.fly.clstudy.domain.member.data.MemberJoinRespBody;
import com.fly.clstudy.global.jpa.dto.EmpClass;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ApiV1MemberController {

    private final MemberService memberService;
    private final ReqData rq;

    @PostMapping("")
    @Transactional
    public RespData<MemberJoinRespBody> join(@RequestBody @Valid MemberJoinReqBody reqBody){

        RespData<Member> joinRs = memberService.join(reqBody.getUsername(), reqBody.getPassword(), reqBody.getNickname());

        return joinRs.newDataOf(
                new MemberJoinRespBody(
                        new MemberDto(
                                joinRs.getData()
                        )
                )
        );
    }


    @PostMapping("/login")
    @Transactional
    public RespData<MemberLoginRespBody> login(
            @RequestBody @Valid MemberLoginReqBody reqBody
    ) {
        Member member = memberService
                .findByUsername(reqBody.getUsername())
                .orElseThrow(() -> new GlobalException("401-1", "해당 회원이 존재하지 않습니다."));

        if (!member.getPassword().equals(reqBody.getPassword())) {
            throw new GlobalException("401-2", "비밀번호가 일치하지 않습니다.");
        }

        rq.setCookie("actorUsername", member.getUsername());
        rq.setCookie("actorPassword", member.getPassword());

        return RespData.of(
                "200-1",
                "로그인 되었습니다.",
                new MemberLoginRespBody(
                        new MemberDto(member)
                )
        );
    }

    @DeleteMapping("/logout")
    @Transactional
    public RespData<EmpClass> logout() {
        rq.removeCookie("actorUsername");
        rq.removeCookie("actorPassword");

        return RespData.OK;
    }

}
