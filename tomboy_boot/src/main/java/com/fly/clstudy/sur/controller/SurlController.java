package com.fly.clstudy.sur.controller;

import com.fly.clstudy.global.exceptions.GlobalException;
import com.fly.clstudy.global.https.RespData;
import com.fly.clstudy.sur.entity.Surl;
import com.fly.clstudy.sur.service.SurlService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SurlController {
    private List<Surl> surls = new ArrayList<>();
    private long surlsLastId;
    private final SurlService surlService;

    @GetMapping("/all")
    @ResponseBody
    public List<Surl> getAll() {
        return surls;
    }

    @GetMapping("/add")
    @ResponseBody
    public RespData<Surl> add(String body, String url) {
        return surlService.add(body, url);
    }
    @GetMapping("/s/{body}/**")
    @ResponseBody
    public RespData<Surl> add(
            @PathVariable String body,
            HttpServletRequest req
    ) {
        String url = req.getRequestURI();

        if ( req.getQueryString() != null ) {
            url += "?" + req.getQueryString();
        }

        String[] urlBits = url.split("/", 4);

        url = urlBits[3];

        return surlService.add(body, url);
    }
    @GetMapping("/g/{id}")
    public String go(
            @PathVariable long id
    ) {

        Surl surl = surlService.findById(id).orElseThrow(GlobalException.E404::new);
        surlService.increaseCount(surl);

        return "redirect:" + surl.getUrl();
    }
}