package com.fly.clstudy.sur.controller;

import com.fly.clstudy.sur.entity.Surl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class SurlController {
    private List<Surl> surls = new ArrayList<>();
    private long surlsLastId;

    @GetMapping("/add")
    @ResponseBody
    public Surl add(String body, String url) {
        Surl surl = Surl
                .builder()
                .id(++surlsLastId)
                .body(body)
                .url(url)
                .build();

        surls.add(surl);

        return surl;
    }
    @GetMapping("/s/{body}/**")
    @ResponseBody
    public Surl add(
            @PathVariable String body,
            HttpServletRequest req
    ) {
        String url = req.getRequestURI();

        if ( req.getQueryString() != null ) {
            url += "?" + req.getQueryString();
        }

        String[] urlBits = url.split("/", 4);

        System.out.println("Arrays.toString(urlBits) : " + Arrays.toString(urlBits));

        url = urlBits[3];

        Surl surl = Surl
                .builder()
                .id(++surlsLastId)
                .body(body)
                .url(url)
                .build();

        surls.add(surl);

        return surl;
    }
}