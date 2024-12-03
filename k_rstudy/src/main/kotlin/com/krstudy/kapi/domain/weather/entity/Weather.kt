package com.krstudy.kapi.domain.weather.entity

import com.krstudy.kapi.global.jpa.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "weather")
class Weather(

    @Column
    val x: Int,
    @Column
    val y: Int,
    @Column
    val hour: Int,
    @Column
    val temp: Double,
    @Column
    val sky: Int,
    @Column
    val pty: Int,
    @Column(length = 255)
    val wfKor: String,
    @Column
    val pop: Int,
    @Column
    val reh: Int
)  : BaseEntity() {}