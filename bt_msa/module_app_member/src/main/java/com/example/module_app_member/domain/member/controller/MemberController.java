package com.example.module_app_member.domain.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/member")
public class MemberController {
    @GetMapping("")
    @ResponseBody
    public String index() {
        int a = 10 + 20;
        return "환영합니다.";
    }
}
