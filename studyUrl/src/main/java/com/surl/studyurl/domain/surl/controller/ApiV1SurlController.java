package com.surl.studyurl.domain.surl.controller;

import com.surl.studyurl.domain.member.entity.Member;
import com.surl.studyurl.domain.member.service.MemberService;
import com.surl.studyurl.domain.surl.data.SurCreateReqBody;
import com.surl.studyurl.domain.surl.data.SurModifyReqBody;
import com.surl.studyurl.domain.surl.service.SurService;
import com.surl.studyurl.global.security.SecurityUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/surls")
@RequiredArgsConstructor
@Slf4j
public class ApiV1SurlController {

    private final SurService surService;
    private final MemberService memberService;

    @PostMapping("")
    public void create(@Valid @RequestBody SurCreateReqBody reqBody, Principal principal){

        //surService.create(reqBody.url,reqBody.title);
        //log.debug("principal: {}",principal);
        SecurityUser user = Principal == null ? null:(SecurityUser)((Authentication)principal).getPrincipal();
        log.debug("user: {}",user);
        Member author =memberService.findByUserNo(4L).get();
        surService.create(author,reqBody.url,reqBody.title);
    }

    @PutMapping("/{id}")
    public void modify(
            @PathVariable long id,
            @Valid @RequestBody SurModifyReqBody reqBody){
        surService.modify(id,reqBody.title);
    }

}
