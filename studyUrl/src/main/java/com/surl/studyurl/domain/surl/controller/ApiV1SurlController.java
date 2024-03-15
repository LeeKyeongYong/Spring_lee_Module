package com.surl.studyurl.domain.surl.controller;

import com.surl.studyurl.domain.surl.data.SurCreateReqBody;
import com.surl.studyurl.domain.surl.data.SurModifyReqBody;
import com.surl.studyurl.domain.surl.service.SurService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/surls")
@RequiredArgsConstructor
public class ApiV1SurlController {

    private final SurService surService;

    @PostMapping("")
    public void create(@Valid @RequestBody SurCreateReqBody reqBody){
        surService.create(reqBody.url,reqBody.title);
    }

    @PutMapping("/{id}")
    public void modify(
            @PathVariable long id,
            @Valid @RequestBody SurModifyReqBody reqBody){
        surService.modify(id,reqBody.title);
    }

}
