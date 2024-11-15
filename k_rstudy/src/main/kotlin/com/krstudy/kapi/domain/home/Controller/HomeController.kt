package com.krstudy.kapi.domain.home.Controller

import com.krstudy.kapi.domain.banners.service.BannerService
import com.krstudy.kapi.domain.popups.entity.DeviceType
import com.krstudy.kapi.domain.popups.service.PopupService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import com.krstudy.kapi.global.https.ReqData
import com.krstudy.kapi.domain.post.service.PostService
import jakarta.servlet.http.HttpServletRequest

@Controller
class HomeController(
    private val rq: ReqData,
    private val postService: PostService,
    private val bannerService: BannerService,
    private val popupService: PopupService
) {
    @GetMapping("/")
    fun showMain(request: HttpServletRequest): String {

        // 디바이스 타입 감지
        val deviceType = if (request.getHeader("User-Agent")?.contains("Mobile") == true) {
            DeviceType.MOBILE
        } else {
            DeviceType.PC
        }

        // 현재 페이지 경로
        val currentPage = request.requestURI
        val popups = popupService.getActivePopups(deviceType, currentPage)
        val banners = bannerService.getActiveBanners()
        rq.setAttribute("banners", banners)

        // 활성화된 팝업 조회
        rq.setAttribute("popups", popups)
        rq.setAttribute("posts", postService.findTop30ByIsPublishedOrderByIdDesc(true))
        return "domain/home/main"
    }
}