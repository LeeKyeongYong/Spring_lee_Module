package com.surl.studyurl.domain.surl.controller;

import com.surl.studyurl.domain.surl.data.SurCreateReqBody;
import com.surl.studyurl.domain.surl.service.SurService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/surls")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApiV1SurlController {

    private final SurService surService;

    @PostMapping("")
    @Transactional
    public void create(@Valid @RequestBody SurCreateReqBody reqBody){
        surService.create(reqBody.url,reqBody.title);
    }

}
