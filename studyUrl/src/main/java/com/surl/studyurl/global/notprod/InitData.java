package com.surl.studyurl.global.notprod;

import com.surl.studyurl.domain.surl.service.SurService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class InitData {

    @Autowired
    @Lazy
    private InitData self;
    private final SurService surService;

    @Bean
    public ApplicationRunner initBasicData(){
        return args -> {
           if(surService.count()>0) return;
           self.work1();
        };
    }

    @Transactional
    public void work1(){
        surService.create("https://59.32.25.14:8080/byeHomeStay","홈스테이");
        surService.create("https://59.32.25.14:8080/AppleMarket/main.do","AppleMarket");
        surService.create("https://59.32.25.14:8080/vueSurvey/index.do","설문조사");
        surService.create("https://59.32.25.14:8080/orgDepts/org.do","조직도프로그램");
        surService.create("https://59.32.25.14:8080/metadata/metaMovie.do","MetaData");
        surService.create("https://59.32.25.14:8080/usrMyInfo/myInfo.do","개발중1");
        surService.create("https://59.32.25.14:8080/skyAir/Plane.do","Plane");
        surService.create("https://59.32.25.14:8080/DangMae/emarket.do","당매");

    }
}
