package com.surl.studyurl.global.httpsdata;

import com.surl.studyurl.domain.member.entity.Member;
import com.surl.studyurl.domain.member.service.MemberService;
import com.surl.studyurl.global.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Optional;

@Component
@RequestScope
@RequiredArgsConstructor
public class ReqData {
    private final MemberService memberService;

    //캐시데이터
    private SecurityUser securityUser;
    private Member member;
    private Boolean isLogin;

    public Member getMember(){
        if(isLogin()) return null;
        if(member == null) member = memberService.getRefenceByNo(getSecurityUser().getId());
        return member;
    }

    private SecurityUser getSecurityUser(){
        if(securityUser!=null) return securityUser;
        securityUser = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(authentication -> authentication.getPrincipal() instanceof  SecurityUser)
                .map(authentication -> (SecurityUser) authentication.getPrincipal())
                .orElse(null);
        isLogin = securityUser!= null;
        return securityUser;
    }

    public boolean isLogout(){
        return !isLogin();
    }

    public boolean isLogin(){
        if(isLogin==null)getSecurityUser();
        return isLogin;
    }

}
