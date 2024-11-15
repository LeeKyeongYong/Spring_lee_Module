package com.krstudy.kapi.domain.popups.dto

import com.krstudy.kapi.domain.popups.enums.PopupStatus
import java.time.LocalDateTime

data class PopupResponse(
    val id: Long,
    val title: String,
    val content: String,
    val imageUrl: String?,
    val linkUrl: String?,
    val status: PopupStatus,
    val priority: Int,
    val width: Int,
    val height: Int,
    val positionX: Int,
    val positionY: Int,
    // ... 다른 필드들
    val viewCount: Long,
    val clickCount: Long,
    val createDate: LocalDateTime
)