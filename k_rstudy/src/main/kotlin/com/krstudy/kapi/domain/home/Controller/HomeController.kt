package com.krstudy.kapi.domain.home.Controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import com.krstudy.kapi.global.https.ReqData
import com.krstudy.kapi.domain.post.service.PostService




@Controller
class HomeController(
    private val rq: ReqData,
    private val postService: PostService
) {
    @GetMapping("/")
    fun showMain(): String {
        rq.setAttribute("posts", postService.findTop30ByIsPublishedOrderByIdDesc(true))
        return "domain/home/main"
    }
}