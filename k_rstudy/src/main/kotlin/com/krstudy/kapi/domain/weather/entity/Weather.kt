package com.krstudy.kapi.domain.weather.entity

import com.krstudy.kapi.domain.weather.dto.WeatherData
import com.krstudy.kapi.global.jpa.BaseEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "weather")
class Weather private constructor(
    @Column val x: Int,
    @Column val y: Int,
    @Column val hour: Int,
    @Embedded val data: WeatherData,
    @Column val timestamp: LocalDateTime = LocalDateTime.now()
) : BaseEntity() {

    // getter 메서드 추가
    fun getTemperature() = data.temperature
    fun getSky() = data.sky
    fun getPrecipitation() = data.precipitation
    fun getDescription() = data.description
    fun getHumidity() = data.humidity
    fun getPrecipitationProbability() = data.precipitationProbability

    companion object {
        fun createWeather(
            x: Int,
            y: Int,
            hour: Int,
            temp: Double,
            sky: Int,
            pty: Int,
            wfKor: String,
            pop: Int,
            reh: Int
        ) = Weather(
            x = x,
            y = y,
            hour = hour,
            data = WeatherData(
                temperature = temp,
                sky = sky,
                precipitation = pty,
                description = wfKor,
                humidity = reh,
                precipitationProbability = pop
            )
        )
    }
}

//@Entity
//@Table(name = "weather")
//class Weather(
//
//    @Column
//    val x: Int,
//    @Column
//    val y: Int,
//    @Column
//    val hour: Int,
//    @Column
//    val temp: Double,
//    @Column
//    val sky: Int,
//    @Column
//    val pty: Int,
//    @Column(length = 255)
//    val wfKor: String,
//    @Column
//    val pop: Int,
//    @Column
//    val reh: Int,
//    @Column
//    val timestamp: LocalDateTime = LocalDateTime.now()
//)  : BaseEntity() {}