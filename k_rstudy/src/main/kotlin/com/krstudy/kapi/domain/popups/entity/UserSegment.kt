package com.krstudy.kapi.domain.popups.entity

import com.krstudy.kapi.global.jpa.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "user_segments")
class UserSegment(

    @Column(nullable = false)
    var name: String,

    @Column
    var description: String? = null,

    @Column(nullable = false)
    var criteria: String, // JSON 형태로 저장된 세그먼트 기준

    @Column(nullable = false)
    var isActive: Boolean = true,

    @ManyToMany(mappedBy = "userSegments")
    var popupTargetings: Set<PopupTargeting> = emptySet()
) : BaseEntity()