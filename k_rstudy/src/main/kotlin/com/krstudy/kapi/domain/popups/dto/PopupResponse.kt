package com.krstudy.kapi.domain.popups.dto

import com.krstudy.kapi.domain.popups.entity.PopupEntity
import com.krstudy.kapi.domain.popups.enums.PopupStatus
import java.time.LocalDateTime

data class PopupResponse(
    val id: Long,
    val title: String,
    val content: String,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
    val status: PopupStatus,
    val priority: Int,
    val width: Int,
    val height: Int,
    val positionX: Int,
    val positionY: Int,
    val imageUrl: String?,
    val linkUrl: String?,
    val altText: String?,
    val target: String,
    val deviceType: String,
    val viewCount: Long,
    val clickCount: Long,
    val createDate: LocalDateTime = LocalDateTime.now()  // 기본값 설정
) {
    companion object {
        fun from(entity: PopupEntity): PopupResponse {
            return PopupResponse(
                id = entity.id,
                title = entity.title,
                content = entity.content,
                startDateTime = entity.startDateTime,
                endDateTime = entity.endDateTime,
                status = entity.status,
                priority = entity.priority,
                width = entity.width,
                height = entity.height,
                positionX = entity.positionX,
                positionY = entity.positionY,
                imageUrl = entity.image?.let { "/files/${it.id}" },
                linkUrl = entity.linkUrl,
                altText = entity.altText,
                target = entity.target.name,
                deviceType = entity.deviceType.name,
                viewCount = entity.viewCount,
                clickCount = entity.clickCount
            )
        }
    }
}