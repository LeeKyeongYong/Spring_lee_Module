package com.surl.studyurl.domain.surl.controller;


import com.surl.studyurl.domain.surl.entity.Surl;
import com.surl.studyurl.domain.surl.service.SurService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class MoveController {
    private final SurService surService;

    @GetMapping("/{id:\\d+}")
    public String move(@PathVariable long id){
        Surl surl = surService.findById(id).get();
        return "redirect:" + surl.getUrl();
    }
}
