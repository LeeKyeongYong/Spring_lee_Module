package com.fly.clstudy.member.controller;

import com.fly.clstudy.global.exceptions.GlobalException;
import com.fly.clstudy.global.https.RespData;
import com.fly.clstudy.member.entity.Member;
import com.fly.clstudy.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/join")
    @ResponseBody
    public RespData join(
            String username, String password, String nickname
    ) {
        if (username == null || username.isBlank()) {
            throw new GlobalException("400-1", "아이디를 입력해주세요.");
        }

        if (password == null || password.isBlank()) {
            throw new GlobalException("400-2", "비밀번호를 입력해주세요.");
        }

        if (nickname == null || nickname.isBlank()) {
            throw new GlobalException("400-3", "닉네임을 입력해주세요.");
        }

        RespData<Member> joinRs = memberService.join(username, password, nickname);

        return joinRs;
    }

}
