package com.krstudy.kapi.domain.popups.entity

@Entity
class PopupScheduleRule(
    // 복잡한 반복 규칙 지원
    var cronExpression: String?,

    // 특정 기간 제외
    @ElementCollection
    var excludeDates: Set<LocalDate>,

    // 특정 시간대 타겟팅
    var targetHours: String?, // "09:00-18:00,19:00-22:00"

    // 요일별 다른 시간 설정
    @ElementCollection
    var dayTimeRules: Map<DayOfWeek, String>
)