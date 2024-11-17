package com.krstudy.kapi.domain.popups.dto

import com.krstudy.kapi.domain.popups.entity.PopupTemplateEntity
import java.time.LocalDateTime

data class TemplateResponse(
    val id: Long,
    val name: String,
    val content: String?,
    val width: Int,
    val height: Int,
    val backgroundColor: String?,
    val borderStyle: String?,
    val isDefault: Boolean,
    val createdBy: String,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(template: PopupTemplateEntity): TemplateResponse {
            return TemplateResponse(
                id = template.id,
                name = template.name,
                content = template.content,  // 이제 nullable 타입이 일치함
                width = template.width,
                height = template.height,
                backgroundColor = template.backgroundColor,
                borderStyle = template.borderStyle,
                isDefault = template.isDefault,
                createdBy = template.creator.userid,
                createdAt = template.getCreateDate() ?: LocalDateTime.now()
            )
        }
    }
}