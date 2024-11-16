package com.krstudy.kapi.domain.popups.entity

import com.krstudy.kapi.global.jpa.BaseEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "popup_statistics")
class PopupStatisticsEntity(

    @Column(nullable = false)
    val popupId: Long,

    @Column(nullable = false)
    val hour: Int,

    @Column(nullable = false)
    var viewCount: Long = 0,  // var로 변경

    @Column(nullable = false)
    var clickCount: Long = 0,  // var로 변경

    @Column(nullable = false)
    var closeCount: Long = 0,  // 추가

    @ElementCollection
    @CollectionTable(
        name = "popup_device_stats",
        joinColumns = [JoinColumn(name = "stats_id")]
    )
    @MapKeyColumn(name = "device_type")
    @Column(name = "count")
    var deviceStats: MutableMap<String, Long> = mutableMapOf(),

    @ElementCollection
    @CollectionTable(
        name = "popup_close_type_stats",
        joinColumns = [JoinColumn(name = "stats_id")]
    )
    @MapKeyColumn(name = "close_type")
    @Column(name = "count")
    var closeTypeStats: MutableMap<String, Long> = mutableMapOf(),

    @Column(nullable = false)
    val conversionCount: Long = 0,

    @Column(nullable = false)
    val viewDuration: Double = 0.0,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val deviceType: DeviceType,

    @Column(nullable = false)
    val recordedAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()  // createdAt 필드 추가
) : BaseEntity()