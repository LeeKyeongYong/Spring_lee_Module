package com.surl.studyurl.domain.surl.controller;

import com.surl.studyurl.domain.surl.data.SurCreateReqBody;
import com.surl.studyurl.domain.surl.data.SurModifyReqBody;
import com.surl.studyurl.domain.surl.service.SurService;
import com.surl.studyurl.global.httpsdata.ReqData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/surls")
@RequiredArgsConstructor
@Slf4j
public class ApiV1SurlController {

    private final SurService surService;
    private final ReqData reqData;

    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public void create(
            //@Valid @RequestBody SurCreateReqBody reqBody
           //,@AuthenticationPrincipal SecurityUser user
       @Valid @RequestBody SurCreateReqBody reqBody) {
        surService.create(reqData.getMember(),reqBody.url,reqBody.title);
        //, Principal principal){
        //surService.create(reqBody.url,reqBody.title);
        //log.debug("principal: {}",principal);
        //SecurityUser user = Principal == null ? null:(SecurityUser)((Authentication)principal).getPrincipal();
        //Member authMember = memberService.findByUserNo(user.getId()).get();
        //log.debug("user: {}",user);
        //Member author = authMember;
        //Member author =memberService.findByUserNo(4L).get();
        //surService.create(author,reqBody.url,reqBody.title);
    }

    @PutMapping("/{id}")
    public void modify(
            @PathVariable long id,
            @Valid @RequestBody SurModifyReqBody reqBody){
        surService.modify(id,reqBody.title);
    }

}
