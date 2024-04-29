package com.fly.clstudy.sur.controller;

import com.fly.clstudy.global.exceptions.GlobalException;
import com.fly.clstudy.global.https.ReqData;
import com.fly.clstudy.global.https.RespData;
import com.fly.clstudy.global.jpa.dto.EmpClass;
import com.fly.clstudy.member.entity.Member;
import com.fly.clstudy.sur.data.SurlAddReqBody;
import com.fly.clstudy.sur.data.SurlAddRespBody;
import com.fly.clstudy.sur.data.SurlGetRespBody;
import com.fly.clstudy.sur.dto.SurlDto;
import com.fly.clstudy.sur.entity.Surl;
import com.fly.clstudy.sur.service.SurlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/surls")
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ApiV1SurlController {
        private final SurlService surlService;
        private final ReqData rq;
        @PostMapping("")
        @Transactional
    public RespData<SurlAddRespBody> add(@RequestBody@Valid SurlAddReqBody reqBody){
            Member member =rq.getMember(); //현재 브라우저로 로그인한 회원
            RespData<Surl> addRs  =surlService.add(member,reqBody.getBody(),reqBody.getUrl());

            return addRs.newDataOf( new SurlAddRespBody(
                    new SurlDto(addRs.getData())
            ));
    }

    @GetMapping("/{id}")
    @Transactional
    public RespData<SurlGetRespBody> get(@PathVariable long id){
            Surl surl = surlService.findById(id).orElseThrow(GlobalException.E404::new);
            return RespData.of(new SurlGetRespBody(
                    new SurlDto(surl)
            ));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public RespData<EmpClass> delete(
            @PathVariable long id
    ) {
        Surl surl = surlService.findById(id).orElseThrow(GlobalException.E404::new);

        surlService.delete(surl);

        return RespData.OK;
    }
}