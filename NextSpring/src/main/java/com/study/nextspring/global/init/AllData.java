package com.study.nextspring.global.init;

import com.study.nextspring.domain.post.post.service.PostService;
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

    @Bean
    ApplicationRunner initDataAll() {
        return (args) -> {
            self.work1();
        };
    }

    @Transactional
    public void work1() {
        if (postService.count() > 0) return;

        postService.write("제목 1", "내용 1");
        postService.write("제목 2", "내용 2");
        postService.write("제목 3", "내용 3");
        postService.write("제목 4", "내용 4");
    }
}
