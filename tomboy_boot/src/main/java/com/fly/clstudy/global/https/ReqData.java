package com.fly.clstudy.global.https;

import com.fly.clstudy.domain.member.entity.Member;
import com.fly.clstudy.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
@RequiredArgsConstructor
public class ReqData {
    private final MemberService memberService;
    private final HttpServletRequest req;
    private final HttpServletRequest resp;
    public Member getMember() {
        return memberService.getReferenceById(1L);
    }

    public String getCurrentUrlPath() {
        return req.getRequestURI();
    }
}
