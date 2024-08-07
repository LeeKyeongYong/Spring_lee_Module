package com.krstudy.kapi.com.krstudy.kapi.domain.home.Controller

import com.krstudy.kapi.com.krstudy.kapi.domain.post.service.PostService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
<<<<<<< Updated upstream
import com.krstudy.kapi.com.krstudy.kapi.global.https.ReqData
import com.krstudy.kapi.domain.post.service.PostService
=======
import com.krstudy.kapi.com.krstudy.kapi.global.https.Req
import com.krstudy.kapi.com.krstudy.kapi.global.https.ReqData
>>>>>>> Stashed changes

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