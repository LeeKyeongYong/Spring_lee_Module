package com.example.module_app_member.global.jpa.initData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@RequiredArgsConstructor
public class AllData {
    private final MemberService memberService;
    @Autowired
    @Lazy
    private All self;

    @Bean
    public ApplicationRunner initAll() {
        return args -> {
            self.work1();
        };
    }

    @Transactional
    public void work1() {
        if (memberService.count() == 0) {
            memberService.join("admin", "1234");
            memberService.join("system", "1234");
        }
    }
}
