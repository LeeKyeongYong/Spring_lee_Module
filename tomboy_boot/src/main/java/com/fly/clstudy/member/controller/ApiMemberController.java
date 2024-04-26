package com.fly.clstudy.member.controller;


import com.fly.clstudy.data.MemberJoinReqBody;
import com.fly.clstudy.global.exceptions.GlobalException;
import com.fly.clstudy.global.https.RespData;
import com.fly.clstudy.global.jpa.dto.UtStr;
import com.fly.clstudy.member.entity.Member;
import com.fly.clstudy.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Slf4j
public class ApiMemberController {

    private final MemberService memberService;

    @PostMapping("")
    public RespData<Member> join(@RequestBody MemberJoinReqBody reqBody){
        if(UtStr.str.isBlank(reqBody.getUsername())){
            throw new GlobalException("400-1","아이디어를 입력해주세요.");
        }
        if(UtStr.str.isBlank(reqBody.getUsername())){
            throw new GlobalException("400-2","비밀번호를 입력해주세요.");
        }
        if(UtStr.str.isBlank(reqBody.getUsername())){
            throw new GlobalException("400-3","닉네임을를 입력해주세요.");
        }
        return memberService.join(reqBody.getUsername(),reqBody.getPassword(),reqBody.getNickname());
    }

}
