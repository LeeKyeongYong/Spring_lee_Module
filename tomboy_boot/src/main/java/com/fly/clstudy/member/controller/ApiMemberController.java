package com.fly.clstudy.member.controller;


import com.fly.clstudy.member.data.MemberJoinReqBody;
import com.fly.clstudy.global.https.RespData;
import com.fly.clstudy.member.data.MemberJoinRespBody;
import com.fly.clstudy.member.dto.MemberDto;
import com.fly.clstudy.member.entity.Member;
import com.fly.clstudy.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Slf4j
public class ApiMemberController {

    private final MemberService memberService;

    @PostMapping("")
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
