package com.krstudy.kapi.domain.popups.entity

/**
 * 팝업 반복 설정 엔티티
 */
@Entity(name = "PopupSchedule")
class PopupScheduleEntity(
    @OneToOne
    @JoinColumn(name = "popup_id")
    val popup: PopupEntity,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var repeatType: RepeatType,

    @Column
    var repeatDays: String?, // 요일 정보 (예: "1,3,5" - 월,수,금)

    @Column
    var repeatMonthDay: Int?, // 매월 특정 일

    @Column(nullable = false)
    var endDate: LocalDateTime?
) : BaseEntity()