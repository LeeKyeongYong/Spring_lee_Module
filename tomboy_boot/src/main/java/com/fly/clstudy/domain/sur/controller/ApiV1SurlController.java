package com.fly.clstudy.domain.sur.controller;

import com.fly.clstudy.domain.auth.service.AuthService;
import com.fly.clstudy.domain.member.entity.Member;
import com.fly.clstudy.domain.member.service.MemberService;
import com.fly.clstudy.domain.sur.data.*;
import com.fly.clstudy.domain.sur.dto.SurlDto;
import com.fly.clstudy.domain.sur.service.SurlService;
import com.fly.clstudy.global.exceptions.GlobalException;
import com.fly.clstudy.global.https.ReqData;
import com.fly.clstudy.global.https.RespData;
import com.fly.clstudy.global.jpa.util.EmpClass;
import com.fly.clstudy.domain.sur.entity.Surl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/surls")
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ApiV1SurlController {

        private final SurlService surlService;
        private final ReqData rq;
        private final AuthService authService;
        private final MemberService memberService;

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

            authService.checkCanGetSurl(rq.getMember(), surl);

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

        authService.checkCanGetSurl(rq.getMember(), surl);

        surlService.delete(surl);

        return RespData.OK;
    }

    @GetMapping("")
    public RespData<SurlGetItemsRespBody> getItems() {

            Member member = rq.getMember();
        List<Surl> surls = surlService.findByAuthorOrderByIdDesc(member);
        return RespData.of(new SurlGetItemsRespBody(surls.stream().map(SurlDto::new).toList()));
    }

    @PutMapping("/{id}")
    @Transactional
    public RespData<SurlModifyRespBody> modify(@PathVariable long id,@RequestBody @Valid SurlModifyReqBody reqBody){
        Surl surl = surlService.findById(id).orElseThrow(GlobalException::new);

        authService.checkCanGetSurl(rq.getMember(), surl);

        RespData<Surl> modifyRs = surlService.modify(surl,reqBody.getBody(),reqBody.getUrl());

        return modifyRs.newDataOf(new SurlModifyRespBody(new SurlDto(modifyRs.getData())));
    }

}