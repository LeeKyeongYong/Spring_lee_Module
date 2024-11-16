package com.krstudy.kapi.domain.popups.entity

import com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.domain.popups.dto.PopupResponse
import com.krstudy.kapi.domain.popups.enums.PopupStatus
import com.krstudy.kapi.domain.uploads.entity.FileEntity
import com.krstudy.kapi.global.jpa.BaseEntity
import jakarta.persistence.*
import java.time.LocalDateTime

/**
 * 팝업 엔티티
 * CMS의 팝업 정보를 저장하는 메인 엔티티
 */
@Entity(name = "Popup")
class PopupEntity(
    @Column(nullable = false, length = 100)
    var title: String, // 팝업 제목

    @Column(nullable = false, length = 2000)
    var content: String, // 팝업 내용

    @Column(nullable = false)
    var startDateTime: LocalDateTime, // 시작 일시

    @Column(nullable = false)
    var endDateTime: LocalDateTime, // 종료 일시

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: PopupStatus = PopupStatus.ACTIVE, // 팝업 상태

    @Column(nullable = false)
    var priority: Int, // 우선순위 (1~10)

    @Column(nullable = false)
    var width: Int, // 팝업 너비

    @Column(nullable = false)
    var height: Int, // 팝업 높이

    @Column(nullable = false)
    var positionX: Int, // X 좌표

    @Column(nullable = false)
    var positionY: Int, // Y 좌표

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    var image: FileEntity?, // 팝업 이미지

    @Column
    var linkUrl: String?, // 링크 URL

    @Column
    var altText: String?, // 대체 텍스트

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var target: PopupTarget = PopupTarget.SELF, // 링크 타겟

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var deviceType: DeviceType = DeviceType.ALL, // 디바이스 타입

    @Column(nullable = false)
    var cookieExpireDays: Int = 1, // 쿠키 만료일

    @Column(nullable = false)
    var hideForToday: Boolean = true, // 오늘 하루 보지 않기 옵션

    @Column(nullable = false)
    var hideForWeek: Boolean = false, // 일주일 동안 보지 않기 옵션

    @Column
    var backgroundColor: String?, // 배경색

    @Column
    var borderStyle: String?, // 테두리 스타일

    @Column(nullable = false)
    var shadowEffect: Boolean = false, // 그림자 효과

    @Column
    @Enumerated(EnumType.STRING)
    var animationType: AnimationType?, // 애니메이션 효과

    @ElementCollection
    @CollectionTable(name = "popup_display_pages")
    var displayPages: Set<String> = setOf(), // 노출할 페이지 목록

    @ElementCollection
    @CollectionTable(name = "popup_target_roles")
    var targetRoles: Set<String> = setOf(), // 타겟 사용자 역할

    @Column(nullable = false)
    var maxDisplayCount: Int = 0, // 최대 노출 횟수 (0은 무제한)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    var creator: Member, // 생성자

    @Column(nullable = false)
    var viewCount: Long = 0, // 조회수

    @Column(nullable = false)
    var clickCount: Long = 0 // 클릭수
) : BaseEntity() {

    /**
     * 팝업 복제 메서드
     */
    fun copy(
        title: String = this.title,
        status: PopupStatus = this.status,
        creator: Member = this.creator
    ): PopupEntity {
        return PopupEntity(
            title = title,
            content = this.content,
            startDateTime = this.startDateTime,
            endDateTime = this.endDateTime,
            status = status,
            priority = this.priority,
            width = this.width,
            height = this.height,
            positionX = this.positionX,
            positionY = this.positionY,
            image = this.image,
            linkUrl = this.linkUrl,
            altText = this.altText,
            target = this.target,
            deviceType = this.deviceType,
            cookieExpireDays = this.cookieExpireDays,
            hideForToday = this.hideForToday,
            hideForWeek = this.hideForWeek,
            backgroundColor = this.backgroundColor,
            borderStyle = this.borderStyle,
            shadowEffect = this.shadowEffect,
            animationType = this.animationType,
            displayPages = this.displayPages,
            targetRoles = this.targetRoles,
            maxDisplayCount = this.maxDisplayCount,
            creator = creator,
            viewCount = 0,
            clickCount = 0
        )
    }

    /**
     * 팝업 응답 DTO 변환 메서드
     */
    fun toResponse(): PopupResponse {
        return PopupResponse(
            id = this.id,
            title = this.title,
            content = this.content,
            startDateTime = this.startDateTime,
            endDateTime = this.endDateTime,
            status = this.status,
            priority = this.priority,
            width = this.width,
            height = this.height,
            positionX = this.positionX,
            positionY = this.positionY,
            imageUrl = this.image?.let { "/api/v1/files/view/${it.id}" },
            linkUrl = this.linkUrl,
            altText = this.altText,
            target = this.target.name,
            deviceType = this.deviceType.name,
            viewCount = this.viewCount,
            clickCount = this.clickCount,
            hideForToday = this.hideForToday,
            hideForWeek = this.hideForWeek,
            createDate = this.getCreateDate() ?: LocalDateTime.now()
        )
    }

    /**
     * 조회수 증가
     */
    fun incrementViewCount() {
        this.viewCount++
    }

    /**
     * 클릭수 증가
     */
    fun incrementClickCount() {
        this.clickCount++
    }

    /**
     * 팝업 활성화 여부 확인
     */
    fun isActive(): Boolean {
        val now = LocalDateTime.now()
        return status == PopupStatus.ACTIVE &&
                now.isAfter(startDateTime) &&
                now.isBefore(endDateTime)
    }

    /**
     * 팝업 상태 변경
     */
    fun changeStatus(newStatus: PopupStatus) {
        this.status = newStatus
    }

    /**
     * 팝업 정보 업데이트
     */
    fun update(
        title: String? = null,
        content: String? = null,
        startDateTime: LocalDateTime? = null,
        endDateTime: LocalDateTime? = null,
        priority: Int? = null,
        width: Int? = null,
        height: Int? = null,
        positionX: Int? = null,
        positionY: Int? = null,
        image: FileEntity? = null,
        linkUrl: String? = null,
        altText: String? = null
    ) {
        title?.let { this.title = it }
        content?.let { this.content = it }
        startDateTime?.let { this.startDateTime = it }
        endDateTime?.let { this.endDateTime = it }
        priority?.let { this.priority = it }
        width?.let { this.width = it }
        height?.let { this.height = it }
        positionX?.let { this.positionX = it }
        positionY?.let { this.positionY = it }
        image?.let { this.image = it }
        linkUrl?.let { this.linkUrl = it }
        altText?.let { this.altText = it }
    }
}