package com.krstudy.kapi.domain.banners.controller

import com.krstudy.kapi.domain.banners.dto.BannerCreateRequest
import com.krstudy.kapi.domain.banners.dto.BannerResponse
import com.krstudy.kapi.domain.banners.service.BannerService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/banners")
class BannerRestController(
    private val bannerService: BannerService
) {
    @PostMapping
    fun createBanner(
        @RequestPart("banner") request: BannerCreateRequest,
        @RequestPart("image") image: MultipartFile,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<BannerResponse> {
        val banner = bannerService.createBanner(request, image, userDetails.username)
        return ResponseEntity.ok(banner)
    }

    @GetMapping
    fun getActiveBanners(): ResponseEntity<List<BannerResponse>> {
        return ResponseEntity.ok(bannerService.getActiveBanners())
    }
}