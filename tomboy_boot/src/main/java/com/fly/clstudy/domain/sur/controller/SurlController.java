package com.fly.clstudy.domain.sur.controller;

import com.fly.clstudy.global.exceptions.GlobalException;
import com.fly.clstudy.global.https.ReqData;
import com.fly.clstudy.global.https.RespData;
import com.fly.clstudy.domain.member.entity.Member;
import com.fly.clstudy.domain.sur.entity.Surl;
import com.fly.clstudy.domain.sur.service.SurlService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SurlController {
    private List<Surl> surls = new ArrayList<>();
    private final ReqData rq;

    private long surlsLastId;
    private final SurlService surlService;

    @GetMapping("/all")
    @ResponseBody
    @Transactional
    public List<Surl> getAll() {
        return surls;
    }

    @GetMapping("/add")
    @ResponseBody
    @Transactional
    public RespData<Surl> add(String body, String url) {

        Member member = rq.getMember(); // 현재 브라우저로 로그인한 회원

        return surlService.add(member, body, url);

    }
    @GetMapping("/s/{body}/**")
    @ResponseBody
    @Transactional
    public RespData<Surl> add(
            @PathVariable String body,
            HttpServletRequest req
    ) {
        Member member = rq.getMember();
        String url = req.getRequestURI();

        if ( req.getQueryString() != null ) {
            url += "?" + req.getQueryString();
        }

        String[] urlBits = url.split("/", 4);

        url = urlBits[3];

        return surlService.add(member, body, url);
    }
    @GetMapping("/g/{id}")
    @Transactional
    public String go(
            @PathVariable long id
    ) {

        Surl surl = surlService.findById(id).orElseThrow(GlobalException.E404::new);
        surlService.increaseCount(surl);

        return "redirect:" + surl.getUrl();
    }
}