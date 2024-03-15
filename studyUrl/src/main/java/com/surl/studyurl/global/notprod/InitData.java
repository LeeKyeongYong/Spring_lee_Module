package com.surl.studyurl.global.notprod;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class InitData {
    @Bean
    public ApplicationRunner initBasicData(){
        return args -> {
           log.debug("데이터 삽입처리");
        };
    }
}
