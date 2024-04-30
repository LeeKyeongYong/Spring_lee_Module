package com.fly.clstudy.global.https;

import com.fly.clstudy.domain.member.entity.Member;
import com.fly.clstudy.domain.member.service.MemberService;
import com.fly.clstudy.global.exceptions.GlobalException;
import com.fly.clstudy.global.jpa.dto.UtStr;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Arrays;

@Component
@RequestScope
@RequiredArgsConstructor
public class ReqData {
    private final MemberService memberService;
    private final HttpServletRequest req;
    private final HttpServletRequest resp;
    private Member member;

    public Member getMember() {

        if(member!=null) return member;

        String actorUsername = getCookieValue("actorUsername", null);
        String actorPassword = getCookieValue("actorPassword", null);

        if (actorUsername == null || actorPassword == null) {
            String authorization = req.getHeader("Authorization");
            if (authorization != null) {
                authorization = authorization.substring("bearer ".length());
                String[] authorizationBits = authorization.split(" ", 2);
                actorUsername = authorizationBits[0];
                actorPassword = authorizationBits.length == 2 ? authorizationBits[1] : null;
            }
        }

        if (UtStr.str.isBlank(actorUsername)) throw new GlobalException("401-1", "인증정보(아이디)를 입력해주세요.");
        if (UtStr.str.isBlank(actorPassword)) throw new GlobalException("401-2", "인증정보(비밀번호)를 입력해주세요.");

        Member loginedMember = memberService.findByUsername(actorUsername).orElseThrow(() -> new GlobalException("403-3", "해당 회원이 존재하지 않습니다."));
        if (!loginedMember.getPassword().equals(actorPassword)) throw new GlobalException("403-4", "비밀번호가 일치하지 않습니다.");

        member = loginedMember;

        return loginedMember;
    }

    private String getCookieValue(String cookieName, String defaultValue) {
        return Arrays.stream(req.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(defaultValue);
    }

    public String getCurrentUrlPath() {
        return req.getRequestURI();
    }
}
