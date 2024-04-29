package com.fly.clstudy.domain.auth.service;

import com.fly.clstudy.domain.member.entity.Member;
import com.fly.clstudy.domain.sur.entity.Surl;
import com.fly.clstudy.global.exceptions.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    // Surl 관련 시작
    public void checkCanGetSurl(Member actor, Surl surl) {
        if (!canGetSurl(actor, surl))
            throw new GlobalException("403-1", "권한이 없습니다.");
    }

    private boolean canGetSurl(Member actor, Surl surl) {
        if (actor == null) return false;
        if (surl == null) return false;

        return actor.equals(surl.getAuthor());
    }


    public void checkCanDeleteSurl(Member actor, Surl surl) {
        if (!canDeleteSurl(actor, surl))
            throw new GlobalException("403-1", "권한이 없습니다.");
    }

    private boolean canDeleteSurl(Member actor, Surl surl) {
        return canGetSurl(actor, surl);
    }


    public void checkCanModifySurl(Member actor, Surl surl) {
        if (!canModifySurl(actor, surl))
            throw new GlobalException("403-1", "권한이 없습니다.");
    }

    private boolean canModifySurl(Member actor, Surl surl) {
        return canGetSurl(actor, surl);
    }
    // Surl 관련 끝
}
