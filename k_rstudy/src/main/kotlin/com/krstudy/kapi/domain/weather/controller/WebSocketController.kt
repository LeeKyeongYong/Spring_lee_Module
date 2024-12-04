package com.krstudy.kapi.domain.weather.controller

import com.krstudy.kapi.domain.weather.dto.WeatherLocation
import com.krstudy.kapi.domain.weather.dto.WeatherResponse
import com.krstudy.kapi.domain.weather.service.WeatherService
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller

@Controller
class WeatherWebSocketController(
    private val weatherService: WeatherService
) {
    @MessageMapping("/weather")
    @SendTo("/topic/weather")
    fun handleWeatherUpdate(location: WeatherLocation): WeatherResponse {
        // 웹소켓 요청이 올 때마다 날씨 데이터 업데이트
        weatherService.updateWeatherData(location.x, location.y)
        val weather = weatherService.getWeather(location.x, location.y)
        return weather?.let {
            WeatherResponse(
                temperature = it.getTemperature(),
                sky = it.getSky(),
                pty = it.getPrecipitation(),
                description = it.getDescription()
            )
        } ?: WeatherResponse(
            temperature = 0.0,
            sky = 0,
            pty = 0,
            description = "정보 없음"
        )
    }
}