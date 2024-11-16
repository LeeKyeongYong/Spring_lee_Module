package com.krstudy.kapi.domain.popups.entity

import com.krstudy.kapi.global.jpa.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "popup_targetings")
class PopupTargeting(
    @ManyToMany
    @JoinTable(
        name = "popup_targeting_segments",
        joinColumns = [JoinColumn(name = "targeting_id")],
        inverseJoinColumns = [JoinColumn(name = "segment_id")]
    )
    var userSegments: MutableSet<UserSegment> = mutableSetOf(),

    @ElementCollection
    @CollectionTable(
        name = "popup_target_regions",
        joinColumns = [JoinColumn(name = "targeting_id")]
    )
    @Column(name = "region")
    var targetRegions: MutableSet<String> = mutableSetOf(),

    @Column(name = "previous_interaction_rule")
    private var _previousInteractionRule: String? = null,

    @Column(name = "min_page_dwell_time")
    private var _minPageDwellTime: Int? = null,

    @OneToOne(mappedBy = "targeting")
    var popup: PopupEntity? = null
) : BaseEntity() {

    // 프로퍼티로 접근 제공
    var previousInteractionRule: String?
        get() = _previousInteractionRule
        set(value) {
            _previousInteractionRule = value
        }

    var minPageDwellTime: Int?
        get() = _minPageDwellTime
        set(value) {
            _minPageDwellTime = value
        }

    /**
     * 세그먼트 추가
     */
    fun addUserSegment(segment: UserSegment) {
        userSegments.add(segment)
        segment.popupTargetings.add(this)
    }

    /**
     * 세그먼트 제거
     */
    fun removeUserSegment(segment: UserSegment) {
        userSegments.remove(segment)
        segment.popupTargetings.remove(this)
    }

    /**
     * 지역 추가
     */
    fun addTargetRegion(region: String) {
        targetRegions.add(region)
    }

    /**
     * 지역 제거
     */
    fun removeTargetRegion(region: String) {
        targetRegions.remove(region)
    }

    /**
     * 팝업 연결
     */
    internal fun connectPopup(popupEntity: PopupEntity?) {
        this.popup = popupEntity
    }
}