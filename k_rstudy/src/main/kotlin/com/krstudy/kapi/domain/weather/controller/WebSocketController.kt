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
    // /app/weather로 메시지가 오면 처리
    @MessageMapping("/weather")
    // 처리 결과를 /topic/weather로 발송
    @SendTo("/topic/weather")
    fun handleWeatherUpdate(location: WeatherLocation): WeatherResponse {
        val weather = weatherService.getWeather(location.x, location.y)
        return WeatherResponse(
            temperature = weather?.temp ?: 0.0,
            sky = weather?.sky ?: 0,
            description = weather?.wfKor ?: "정보 없음"
        )
    }
}