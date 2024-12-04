package com.krstudy.kapi.domain.weather.repository

import com.krstudy.kapi.domain.weather.entity.Weather
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface WeatherRepository: JpaRepository<Weather, Long> {
    fun save(weather: Weather)
    //fun findByLocation(x: Int, y: Int): Weather?
    fun findByXAndY(x: Int, y: Int): Weather?
    fun findByXAndYOrderByTimestampDesc(x: Int, y: Int): List<Weather>
    // 가장 최근의 단일 날씨 정보를 가져오는 메서드 추가
    @Query("SELECT w FROM Weather w WHERE w.x = :x AND w.y = :y ORDER BY w.timestamp DESC LIMIT 1")
    fun findLatestWeatherByXAndY(x: Int, y: Int): Weather?
}