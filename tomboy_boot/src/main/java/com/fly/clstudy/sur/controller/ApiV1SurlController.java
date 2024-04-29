package com.fly.clstudy.sur.controller;

import com.fly.clstudy.global.https.ReqData;
import com.fly.clstudy.global.https.RespData;
import com.fly.clstudy.member.entity.Member;
import com.fly.clstudy.sur.data.SurlAddReqBody;
import com.fly.clstudy.sur.data.SurlAddRespBody;
import com.fly.clstudy.sur.entity.Surl;
import com.fly.clstudy.sur.service.SurlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/surls")
@RequiredArgsConstructor
@Slf4j
public class ApiV1SurlController {
        private final SurlService surlService;
        private final ReqData rq;
        @PostMapping("")
    public RespData<SurlAddRespBody> add(@RequestBody@Valid SurlAddReqBody reqBody){
            Member member =rq.getMember(); //현재 브라우저로 로그인한 회원
            RespData<Surl> addRs  =surlService.add(member,reqBody.getBody(),reqBody.getUrl());

            return addRs.newDataOf(new SurlAddRespBody(addRs.getData()));
        }
}