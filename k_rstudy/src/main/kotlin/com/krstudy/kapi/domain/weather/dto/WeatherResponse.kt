package com.krstudy.kapi.domain.weather.dto

data class WeatherResponse(
    val temperature: Double,
    val sky: Int,
    val description: String
)