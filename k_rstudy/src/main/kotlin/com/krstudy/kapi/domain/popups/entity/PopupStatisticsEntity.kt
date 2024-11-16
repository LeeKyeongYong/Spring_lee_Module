package com.krstudy.kapi.domain.popups.entity

/**
 * 팝업 통계 엔티티
 */
@Entity(name = "PopupStatistics")
class PopupStatisticsEntity(
    @OneToOne
    @JoinColumn(name = "popup_id")
    val popup: PopupEntity,

    @Column(nullable = false)
    var viewCount: Long = 0,

    @Column(nullable = false)
    var clickCount: Long = 0,

    @Column(nullable = false)
    var closeCount: Long = 0,

    @ElementCollection
    @CollectionTable(name = "popup_device_stats")
    var deviceStats: MutableMap<String, Long> = mutableMapOf(),

    @ElementCollection
    @CollectionTable(name = "popup_close_type_stats")
    var closeTypeStats: MutableMap<String, Long> = mutableMapOf()
) : BaseEntity()