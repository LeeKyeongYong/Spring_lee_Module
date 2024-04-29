package com.fly.clstudy.domain.member.controller;


import com.fly.clstudy.domain.member.dto.MemberDto;
import com.fly.clstudy.domain.member.entity.Member;
import com.fly.clstudy.domain.member.service.MemberService;
import com.fly.clstudy.domain.member.data.MemberJoinReqBody;
import com.fly.clstudy.global.https.RespData;
import com.fly.clstudy.domain.member.data.MemberJoinRespBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ApiMemberController {

    private final MemberService memberService;

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

}
