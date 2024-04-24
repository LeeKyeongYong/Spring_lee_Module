package com.fly.clstudy.global.https;

import com.fly.clstudy.member.entity.Member;
import com.fly.clstudy.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
@RequiredArgsConstructor
public class ReqData {
    private final MemberService memberService;

    public Member getMember() {
        return memberService.getReferenceById(1L);
    }
}
