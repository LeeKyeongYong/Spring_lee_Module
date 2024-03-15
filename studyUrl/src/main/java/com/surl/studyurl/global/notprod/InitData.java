package com.surl.studyurl.global.notprod;

import com.surl.studyurl.domain.surl.service.SurService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class InitData {

    private final SurService surlService;

    @Bean
    public ApplicationRunner initBasicData(){
        return args -> {
           if(surlService.count()>0) return;
           log.debug("데이터 처리");
        };
    }
}
