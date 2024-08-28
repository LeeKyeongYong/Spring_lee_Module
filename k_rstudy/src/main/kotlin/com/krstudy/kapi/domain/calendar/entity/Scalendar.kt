package com.krstudy.kapi.domain.calendar.entity

import com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.global.jpa.BaseEntity
import jakarta.persistence.*
import java.util.ArrayList

@Entity
class Scalendar(

    @ManyToOne(fetch = FetchType.LAZY)
    var author: Member? = null,

    var title: String? = null,

    @Column(columnDefinition = "TEXT")
    var body: String? = null,

    var hit: Long = 0,
    var startDay: String? = null, // 시작일
    var endDay: String? = null, // 종료일
    var fColor: String? = null // 색상코드
) : BaseEntity() {

    fun increaseHit() {
        hit++
    }

    fun getAuthorUsername(): String? {
        return author?.username
    }
}
