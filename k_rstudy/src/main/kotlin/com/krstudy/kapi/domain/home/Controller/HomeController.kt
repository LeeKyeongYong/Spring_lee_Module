package com.krstudy.kapi.domain.home.Controller

import com.krstudy.kapi.domain.banners.service.BannerService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import com.krstudy.kapi.global.https.ReqData
import com.krstudy.kapi.domain.post.service.PostService

@Controller
class HomeController(
    private val rq: ReqData,
    private val postService: PostService,
    private val bannerService: BannerService
) {
    @GetMapping("/")
    fun showMain(): String {
        val banners = bannerService.getActiveBanners()
        rq.setAttribute("banners", banners)
        rq.setAttribute("posts", postService.findTop30ByIsPublishedOrderByIdDesc(true))
        return "domain/home/main"
    }
}