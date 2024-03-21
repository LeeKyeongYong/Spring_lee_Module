package com.surl.studyurl.global.notprod;

import com.surl.studyurl.domain.member.entity.Member;
import com.surl.studyurl.domain.member.service.MemberService;
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
    private final MemberService memberService;

    @Bean
    public ApplicationRunner initBasicData(){
        return args -> {
           if(surService.count()>0) return;
           self.work1();
        };
    }

    @Transactional
    public void work1(){

        Member memberSystem =  memberService.create("system","1234","system");
        memberSystem.setRefreshToken("system");
        Member memberAdmin = memberService.create("admin","1234","admin");
        memberAdmin.setRefreshToken("admin");
        Member memberGrage = memberService.create("garage","1234","garage");
        memberGrage.setRefreshToken("garage");
        Member memberUser1 = memberService.create("user1","1234","user1");
        memberUser1.setRefreshToken("user1");
        Member memberUser2 = memberService.create("user2","1234","user2");
        memberUser2.setRefreshToken("user2");
        Member memberUser3 = memberService.create("user3","1234","user3");
        memberUser3.setRefreshToken("user3");
        Member memberUser4 = memberService.create("user4","1234","user4");
        memberUser4.setRefreshToken("user4");


        surService.create(memberSystem,"https://59.32.25.14:8080/byeHomeStay","홈스테이");
        surService.create(memberAdmin,"https://59.32.25.14:8080/AppleMarket/main.do","AppleMarket");
        surService.create(memberGrage,"https://59.32.25.14:8080/vueSurvey/index.do","설문조사");
        surService.create(memberUser1,"https://59.32.25.14:8080/orgDepts/org.do","조직도프로그램");
        surService.create(memberUser2,"https://59.32.25.14:8080/metadata/metaMovie.do","MetaData");
        surService.create(memberUser3,"https://59.32.25.14:8080/usrMyInfo/myInfo.do","개발중1");
        surService.create(memberUser4,"https://59.32.25.14:8080/skyAir/Plane.do","Plane");
        surService.create(memberAdmin,"https://59.32.25.14:8080/DangMae/emarket.do","당매");

    }
}
