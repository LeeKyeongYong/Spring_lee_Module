package com.krstudy.kapi.domain.weather.service

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.krstudy.kapi.domain.weather.entity.Weather
import com.krstudy.kapi.domain.weather.repository.WeatherRepository
import org.jdom2.Document
import org.jdom2.Element
import org.jdom2.input.SAXBuilder
import org.springframework.stereotype.Service
import java.net.URL

@Service
class WeatherService(private val weatherRepository: WeatherRepository) {

    private val rssFeed = "http://www.kma.go.kr/wid/queryDFS.jsp?gridx=%s&gridy=%s"

    fun updateWeatherData(): Either<Throwable, Unit> {
        return try {
            val locations = listOf(Pair(59, 125)) // 예시 좌표 리스트
            locations.forEach { (x, y) ->
                val weatherData = fetchWeatherData(x, y)
                weatherData.map { weatherRepository.save(it) }
            }
            Unit.right()
        } catch (e: Exception) {
            e.left()
        }
    }

    private fun fetchWeatherData(x: Int, y: Int): Either<Throwable, Weather> {
        return try {
            val url = String.format(rssFeed, x, y)
            val document: Document = SAXBuilder().build(URL(url))
            val root: Element = document.rootElement
            val body: Element = root.getChild("body")
            val data: Element = body.getChildren("data").first()

            val weather = Weather(
                x = x,
                y = y,
                hour = data.getChildText("hour").toInt(),
                temp = data.getChildText("temp").toDouble(),
                sky = data.getChildText("sky").toInt(),
                pty = data.getChildText("pty").toInt(),
                wfKor = data.getChildText("wfKor"),
                pop = data.getChildText("pop").toInt(),
                reh = data.getChildText("reh").toInt()
            )

            weather.right()
        } catch (e: Exception) {
            e.left()
        }
    }

    fun getWeather(x: Int, y: Int): Weather? {
        return weatherRepository.findByXAndY(x, y)
    }
}