package com.krstudy.kapi.domain.popups.entity

import com.krstudy.kapi.domain.popups.enums.RepeatType
import com.krstudy.kapi.global.jpa.BaseEntity
import jakarta.persistence.*
import java.time.LocalDateTime

/**
 * 팝업 반복 설정 엔티티
 */
@Entity
@Table(name = "popup_schedules")
class PopupScheduleEntity(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "popup_id")
    var popup: PopupEntity,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var repeatType: RepeatType,

    @Column
    var repeatDays: String? = null,  // 주간 반복 시 요일 (1,2,3,4,5,6,7)

    @Column
    var repeatMonthDay: Int? = null, // 월간 반복 시 일자

    @Column(nullable = false)
    var startTime: LocalDateTime,

    @Column(nullable = false)
    var endTime: LocalDateTime,

    @Column(nullable = false)
    var isActive: Boolean = true
) : BaseEntity()