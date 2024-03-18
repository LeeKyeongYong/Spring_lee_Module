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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/surls")
@RequiredArgsConstructor
@Slf4j
public class ApiV1SurlController {

    private final SurService surService;
    private final MemberService memberService;

    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    public void create(@Valid @RequestBody SurCreateReqBody reqBody
           ,@AuthenticationPrincipal SecurityUser user) {
           //, Principal principal){

        //surService.create(reqBody.url,reqBody.title);
        //log.debug("principal: {}",principal);
        //SecurityUser user = Principal == null ? null:(SecurityUser)((Authentication)principal).getPrincipal();
        Member authMember = memberService.findByUserNo(user.getId()).get();
        log.debug("user: {}",user);
        Member author = authMember;
        //Member author =memberService.findByUserNo(4L).get();
        surService.create(author,reqBody.url,reqBody.title);
    }

    @PutMapping("/{id}")
    public void modify(
            @PathVariable long id,
            @Valid @RequestBody SurModifyReqBody reqBody){
        surService.modify(id,reqBody.title);
    }

}
