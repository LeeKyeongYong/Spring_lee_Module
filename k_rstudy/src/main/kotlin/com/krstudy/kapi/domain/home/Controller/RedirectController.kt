package com.krstudy.kapi.domain.home.Controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.view.RedirectView
import java.net.URLDecoder


@Controller
class RedirectController {

    @GetMapping("/redirect")
    fun handleRedirect(encodedUrl: String): RedirectView {
        val decodedUrl = URLDecoder.decode(encodedUrl, "UTF-8")
        return RedirectView(decodedUrl)
    }
}