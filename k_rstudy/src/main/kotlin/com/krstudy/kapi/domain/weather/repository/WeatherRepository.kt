package com.krstudy.kapi.domain.weather.repository

import com.krstudy.kapi.domain.weather.entity.Weather
import org.springframework.data.jpa.repository.JpaRepository

interface WeatherRepository: JpaRepository<Weather, Long> {
    fun save(weather: Weather)
    fun findByLocation(x: Int, y: Int): Weather?
    fun findByXAndY(x: Int, y: Int): Weather?
}