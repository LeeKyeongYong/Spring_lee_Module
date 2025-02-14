package com.study.nextspring.global.init;

import com.study.nextspring.domain.member.entity.Member;
import com.study.nextspring.domain.member.service.MemberService;
import com.study.nextspring.domain.post.entity.Post;
import com.study.nextspring.domain.post.service.PostService;
import com.study.nextspring.global.app.AppConfig;
import com.study.nextspring.global.base.UtClass;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@Profile("dev")
@Configuration
@RequiredArgsConstructor
public class BaseInitData {
    private final MemberService memberService;
    private final PostService postService;
    @Autowired
    @Lazy
    private BaseInitData self;

    @Bean
    public ApplicationRunner baseInitDataApplicationRunner() {
        return args -> {
            self.work1();
            self.work2();
        };
    }

    @Bean
    public ApplicationRunner DataInitDataApplicationRunner() {
        return args -> {
            UtClass.file.downloadByHttp("http://localhost:9090/v3/api-docs/apiV1", ".");
            String cmd = "yes | npx --package typescript --package openapi-typescript openapi-typescript apiV1.json -o frontend/src/lib/backend/apiV1/schema.d.ts";
            UtClass.cmd.runAsync(cmd);
        };
    }

    @Transactional
    public void work1() {
        if (memberService.count() > 0) return;

        Member memberSystem = memberService.join("system2", "1234", "시스템");
        if (AppConfig.isNotProd()) memberSystem.setApiKey("system2");

        Member memberAdmin = memberService.join("admin2", "1234", "관리자");
        if (AppConfig.isNotProd()) memberAdmin.setApiKey("admin2");

        Member memberUser1 = memberService.join("user5", "1234", "유저5");
        if (AppConfig.isNotProd()) memberUser1.setApiKey("user5");

        Member memberUser2 = memberService.join("user6", "1234", "유저6");
        if (AppConfig.isNotProd()) memberUser2.setApiKey("user6");

        Member memberUser3 = memberService.join("user7", "1234", "유저7");
        if (AppConfig.isNotProd()) memberUser3.setApiKey("user7");

        Member memberUser4 = memberService.join("user8", "1234", "유저8");
        if (AppConfig.isNotProd()) memberUser4.setApiKey("user8");

        Member memberUser5 = memberService.join("user9", "1234", "유저9");
        if (AppConfig.isNotProd()) memberUser5.setApiKey("user9");

        Member memberUser6 = memberService.join("user10", "1234", "유저10");
        if (AppConfig.isNotProd()) memberUser5.setApiKey("user10");
    }

    @Transactional
    public void work2() {
        if (postService.count() > 0) return;

        Member memberUser1 = memberService.findByUsername("user5").get();
        Member memberUser2 = memberService.findByUsername("user6").get();
        Member memberUser3 = memberService.findByUsername("user7").get();
        Member memberUser4 = memberService.findByUsername("user8").get();
        Member memberUser5 = memberService.findByUsername("user9").get();
        Member memberUser6 = memberService.findByUsername("user10").get();

        Post post1 = postService.write(
                memberUser1,
                "축구 하실 분?",
                "14시 까지 22명을 모아야 합니다.",
                true,
                true
        );
        post1.addComment(memberUser2, "저요!");
        post1.addComment(memberUser3, "저도 할래요.");

        Post post2 = postService.write(
                memberUser1,
                "배구 하실 분?",
                "15시 까지 12명을 모아야 합니다.",
                true,
                true
        );
        post2.addComment(memberUser4, "저요!, 저 배구 잘합니다.");

        Post post3 = postService.write(
                memberUser2,
                "농구 하실 분?",
                "16시 까지 10명을 모아야 합니다.",
                true,
                true
        );

        Post post4 = postService.write(
                memberUser3,
                "발야구 하실 분?",
                "17시 까지 14명을 모아야 합니다.",
                true,
                true
        );

        Post post5 = postService.write(
                memberUser4,
                "피구 하실 분?",
                "18시 까지 18명을 모아야 합니다.",
                true,
                true
        );

        Post post6 = postService.write(
                memberUser4,
                "발야구를 밤에 하실 분?",
                "22시 까지 18명을 모아야 합니다.",
                false,
                false
        );

        Post post7 = postService.write(
                memberUser4,
                "발야구를 새벽 1시에 하실 분?",
                "새벽 1시 까지 17명을 모아야 합니다.",
                true,
                false
        );

        Post post8 = postService.write(
                memberUser4,
                "발야구를 새벽 3시에 하실 분?",
                "새벽 3시 까지 19명을 모아야 합니다.",
                false,
                true
        );

        IntStream.rangeClosed(9, 100).forEach(
                i -> postService.write(
                        memberUser5,
                        "테스트 게시물 " + i,
                        "테스트 게시물 " + i + " 내용",
                        i % 3 != 0,
                        i % 4 != 0
                )
        );

        IntStream.rangeClosed(101, 200).forEach(
                i -> postService.write(
                        memberUser6,
                        "테스트 게시물 " + i,
                        "테스트 게시물 " + i + " 내용",
                        i % 5 != 0,
                        i % 6 != 0
                )
        );

    }
}
