package com.krstudy.kapi.domain.popups.entity

import com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.global.jpa.BaseEntity
import jakarta.persistence.*
import java.time.LocalDateTime

/**
 * 팝업 수정 이력 엔티티
 */
@Entity
@Table(name = "popup_history")  // 테이블 이름 명시적 지정
class PopupHistoryEntity(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "popup_id")
    val popup: PopupEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "editor_id")
    val editor: Member,

    @Column(nullable = false)
    val action: String, // CREATE, UPDATE, DELETE, ACTIVATE, DEACTIVATE

    @Column(columnDefinition = "TEXT")
    val changeDetails: String?, // JSON 형태로 변경 내용 저장, null 허용

    @Column(nullable = false)
    val actionDate: LocalDateTime = LocalDateTime.now()
) : BaseEntity()