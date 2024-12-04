package com.krstudy.kapi.domain.weather.dto

data class WeatherData(
    val temperature: Double,
    val sky: Int,
    val precipitation: Int,
    val description: String,
    val humidity: Int,
    val precipitationProbability: Int
)