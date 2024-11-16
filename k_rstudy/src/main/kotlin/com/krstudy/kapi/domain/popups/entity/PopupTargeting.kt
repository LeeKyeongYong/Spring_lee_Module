package com.krstudy.kapi.domain.popups.entity


import com.krstudy.kapi.global.jpa.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "popup_targetings")
class PopupTargeting(

    // 사용자 세그먼트 설정
    @ManyToMany
    @JoinTable(
        name = "popup_targeting_segments",
        joinColumns = [JoinColumn(name = "targeting_id")],
        inverseJoinColumns = [JoinColumn(name = "segment_id")]
    )
    var userSegments: Set<UserSegment> = emptySet(),

    // 접속 위치 기반 타겟팅
    @ElementCollection
    @CollectionTable(
        name = "popup_target_regions",
        joinColumns = [JoinColumn(name = "targeting_id")]
    )
    @Column(name = "region")
    var targetRegions: Set<String> = emptySet(),

    // 이전 팝업 반응 기반 타겟팅
    @Column
    var previousInteractionRule: String? = null, // "NOT_CLICKED_7DAYS"

    // 페이지 체류시간 기반 표시
    @Column
    var minPageDwellTime: Int? = null, // 초 단위

    @OneToOne(mappedBy = "targeting")
    var popup: PopupEntity? = null
) : BaseEntity()