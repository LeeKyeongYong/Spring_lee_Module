package com.krstudy.kapi.domain.weather.controller

import com.krstudy.kapi.domain.weather.service.ImageService
import com.krstudy.kapi.domain.weather.service.WeatherService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class WeatherController(
    private val weatherService: WeatherService,
    private val imageService: ImageService
) {

    @GetMapping("/weather")
    fun getWeather(model: Model): String {
        val weather = weatherService.getWeather(59, 125)
        model.addAttribute("weather", weather)
        return "weather"
    }

    @GetMapping("/weather/image/{x}/{y}")
    @ResponseBody
    fun getWeatherImage(@PathVariable x: Int, @PathVariable y: Int): ByteArray {
        val weather = weatherService.getWeather(x, y)
        return weather?.let { imageService.generateWeatherImage(it) } ?: ByteArray(0)
    }
}