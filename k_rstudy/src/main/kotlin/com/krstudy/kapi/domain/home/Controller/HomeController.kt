package com.krstudy.kapi.com.krstudy.kapi.domain.home.Controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import com.krstudy.kapi.com.krstudy.kapi.global.https.Req
@Controller
class HomeController(
    private val rq: Req,
    private val postService: PostService
) {

    @GetMapping("/")
    fun showMain(): String {
        rq.setAttribute("posts", postService.findTop30ByIsPublishedOrderByIdDesc(true))
        return "domain/home/main"
    }
}