package com.fly.clstudy.member.controller;

import com.fly.clstudy.global.exceptions.GlobalException;
import com.fly.clstudy.global.https.RespData;
import com.fly.clstudy.global.jpa.dto.UtStr;
import com.fly.clstudy.member.entity.Member;
import com.fly.clstudy.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/join")
    @ResponseBody
    public RespData<Member> join(
            String username, String password, String nickname
    ) {
        if (UtStr.str.isBlank(username)) {
            throw new GlobalException("400-1", "아이디를 입력해주세요.");
        }

        if (UtStr.str.isBlank(password)) {
            throw new GlobalException("400-2", "비밀번호를 입력해주세요.");
        }

        if (UtStr.str.isBlank(nickname)) {
            throw new GlobalException("400-3", "닉네임을 입력해주세요.");
        }

        return memberService.join(username, password, nickname);
    }

    @GetMapping("/testThrowIllegalArgumentException")
    @ResponseBody
    public RespData<Member> testThrowIllegalArgumentException() {
        throw new IllegalArgumentException("IllegalArgumentException");
    }

}
