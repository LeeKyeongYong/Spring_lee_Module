package com.example.module_app_member.global.jpa.initData;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

@Profile("!prod")
@Configuration
@RequiredArgsConstructor
public class NotProd {
    private final MemberService memberService;
    @Autowired
    @Lazy
    private NotProd self;

    @Bean
    public ApplicationRunner initNotProd() {
        return args -> {
            self.work1();
        };
    }

    @Transactional
    public void work1() {

    }
}