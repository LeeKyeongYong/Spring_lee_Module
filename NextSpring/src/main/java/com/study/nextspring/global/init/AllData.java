package com.study.nextspring.global.init;

import com.study.nextspring.domain.member.entity.Member;
import com.study.nextspring.domain.member.service.MemberService;
import com.study.nextspring.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
public class AllData {
    @Autowired
    @Lazy
    private AllData self;
    private final PostService postService;
    private final MemberService memberService;

    @Bean
    ApplicationRunner initDataAll() {
        return (args) -> {
            self.work1();
        };
    }

    @Transactional
    public void work1() {

        if (memberService.count() > 0) return;

        Member memberSystem = memberService.join("system", "1234", "시스템");
        Member memberAdmin = memberService.join("admin", "1234", "관리자");
        Member memberUser1 = memberService.join("user1", "1234", "유저1");
        Member memberUser2 = memberService.join("user2", "1234", "유저2");
        Member memberUser3 = memberService.join("user3", "1234", "유저3");

        if (postService.count() > 0) return;

        postService.write(memberUser1, "제목 1", "내용 1", true, true);
        postService.write(memberUser1, "제목 2", "내용 2", true, true);
        postService.write(memberUser2, "제목 3", "내용 3", true, false);
        postService.write(memberUser3, "제목 4", "내용 4", false, false);

        for (int i = 5; i <= 100; i++) {
            postService.write(memberUser3, "제목 " + i, "내용 " + i, true, true);
        }
    }
}
