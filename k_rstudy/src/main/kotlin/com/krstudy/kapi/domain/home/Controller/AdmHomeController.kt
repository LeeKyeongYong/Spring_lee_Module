package com.krstudy.kapi.com.krstudy.kapi.domain.home.Controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/adm")
class AdmHomeController {

    @GetMapping("")
    fun showMain(): String {
        return "domain/home/adm/main"
    }

    @GetMapping("/home/about")
    fun showAbout(): String {
        return "domain/home/adm/about"
    }
}
