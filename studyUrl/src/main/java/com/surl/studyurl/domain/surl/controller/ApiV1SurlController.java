package com.surl.studyurl.domain.surl.controller;

import com.surl.studyurl.domain.surl.data.SurCreateReqBody;
import com.surl.studyurl.domain.surl.data.SurModifyReqBody;
import com.surl.studyurl.domain.surl.data.SurlCreateRespBody;
import com.surl.studyurl.domain.surl.entity.Surl;
import com.surl.studyurl.domain.surl.service.SurService;
import com.surl.studyurl.global.httpsdata.ReqData;
import com.surl.studyurl.global.httpsdata.RespData;
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
    public RespData<SurlCreateRespBody> create(@Valid @RequestBody SurCreateReqBody reqBody) {
        Surl surl = surService.create(reqData.getMember(), reqBody.url(),reqBody.title());
        System.out.println("reqData.getMember(): "+reqData.getMember());
        return RespData.of(new SurlCreateRespBody(surl.getShortUrl()));
    }

    @PutMapping("/{id}")
    public void modify(
            @PathVariable long id,
            @Valid @RequestBody SurModifyReqBody reqBody){
        surService.modify(id,reqBody.title);
    }

}
