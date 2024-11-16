package com.krstudy.kapi.domain.popups.entity

@Entity
class PopupTargeting(
    // 사용자 세그먼트 설정
    @ManyToMany
    var userSegments: Set<UserSegment>,

    // 접속 위치 기반 타겟팅
    var targetRegions: Set<String>,

    // 이전 팝업 반응 기반 타겟팅
    var previousInteractionRule: String?, // "NOT_CLICKED_7DAYS"

    // 페이지 체류시간 기반 표시
    var minPageDwellTime: Int? // 초 단위
)