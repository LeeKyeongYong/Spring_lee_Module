package com.krstudy.kapi.domain.popups.entity

import com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.domain.popups.dto.TemplateResponse
import com.krstudy.kapi.global.jpa.BaseEntity
import jakarta.persistence.*

/**
 * 팝업 템플릿 엔티티
 */
@Entity(name = "PopupTemplate")
class PopupTemplateEntity(
    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var content: String,

    @Column(nullable = false)
    var width: Int,

    @Column(nullable = false)
    var height: Int,

    @Column
    var backgroundColor: String?,

    @Column
    var borderStyle: String?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    var creator: Member,

    @Column(nullable = false)
    var isDefault: Boolean = false
) : BaseEntity() {

    /**
     * 템플릿 응답 DTO로 변환
     */
    fun toResponse(): TemplateResponse {
        return TemplateResponse.from(this)
    }

    /**
     * 템플릿 정보 업데이트
     */
    fun update(
        name: String? = null,
        content: String? = null,
        width: Int? = null,
        height: Int? = null,
        backgroundColor: String? = null,
        borderStyle: String? = null,
        isDefault: Boolean? = null
    ) {
        name?.let { this.name = it }
        content?.let { this.content = it }
        width?.let { this.width = it }
        height?.let { this.height = it }
        backgroundColor?.let { this.backgroundColor = it }
        borderStyle?.let { this.borderStyle = it }
        isDefault?.let { this.isDefault = it }
    }

    /**
     * 템플릿 복제
     */
    fun copy(
        name: String = this.name,
        creator: Member = this.creator,
        isDefault: Boolean = false
    ): PopupTemplateEntity {
        return PopupTemplateEntity(
            name = "$name (복사본)",
            content = this.content,
            width = this.width,
            height = this.height,
            backgroundColor = this.backgroundColor,
            borderStyle = this.borderStyle,
            creator = creator,
            isDefault = isDefault
        )
    }
}