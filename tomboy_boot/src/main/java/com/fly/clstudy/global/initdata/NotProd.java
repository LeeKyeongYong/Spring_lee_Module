package com.fly.clstudy.global.initdata;

import com.fly.clstudy.domain.article.entity.Article;
import com.fly.clstudy.domain.article.service.ArticleService;
import com.fly.clstudy.domain.member.entity.Member;
import com.fly.clstudy.domain.member.service.MemberService;
import com.fly.clstudy.domain.sur.entity.Surl;
import com.fly.clstudy.domain.sur.service.SurlService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

// !prod == dev or test
@Profile("!prod")
@Configuration
@RequiredArgsConstructor
public class NotProd {
    @Lazy
    @Autowired
    private NotProd self;
    private final MemberService memberService;
    private final ArticleService articleService;
    private final SurlService surlService;

    @Bean
    @Order(4)
    public ApplicationRunner initNotProd() {
        return args -> {
            self.work1();
        };
    }
        @Transactional
        public void work1() {
            if (articleService.count() > 0) return;

            Member memberUser1 = memberService.findByUsername("user1").get();
            Member memberUser2 = memberService.findByUsername("user2").get();

            Article article1 = articleService.write(memberUser1, "제목 1", "내용 1").getData();
            Article article2 = articleService.write(memberUser1, "제목 2", "내용 2").getData();

            Article article3 = articleService.write(memberUser2, "제목 3", "내용 3").getData();
            Article article4 = articleService.write(memberUser2, "제목 4", "내용 4").getData();

            Surl surl1 = surlService.add(memberUser1, "네이버", "https://www.naver.com").getData();
            Surl surl2 = surlService.add(memberUser1, "다음", "https://www.daum.net").getData();

            Surl surl3 = surlService.add(memberUser2, "구글", "https://www.google.com").getData();
            Surl surl4 = surlService.add(memberUser2, "네이버", "https://www.naver.com").getData();


        }
}