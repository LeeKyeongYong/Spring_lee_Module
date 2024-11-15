package com.krstudy.kapi.domain.popups.dto

/**
 * 팝업 생성 요청 DTO
 */
data class PopupCreateRequest(
    val title: String,
    val content: String,
    val startDateTime: String,
    val endDateTime: String,
    val priority: Int,
    val width: Int,
    val height: Int,
    val positionX: Int,
    val positionY: Int,
    val linkUrl: String?,
    val altText: String?,
    val target: String,
    val deviceType: String,
    val cookieExpireDays: Int,
    val hideForToday: Boolean,
    val hideForWeek: Boolean,
    val backgroundColor: String?,
    val borderStyle: String?,
    val shadowEffect: Boolean,
    val animationType: String?,
    val displayPages: Set<String>,
    val targetUserGroups: Set<Long>,
    val maxDisplayCount: Int
)