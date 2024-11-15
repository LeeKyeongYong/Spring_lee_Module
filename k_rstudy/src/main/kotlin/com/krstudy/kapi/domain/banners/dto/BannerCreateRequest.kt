package com.krstudy.kapi.domain.banners.dto

import java.time.LocalDateTime

data class BannerCreateRequest(
    val title: String,
    val description: String,
    val linkUrl: String?,
    val displayOrder: Int,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime
)
