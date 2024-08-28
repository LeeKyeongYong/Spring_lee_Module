package com.krstudy.kapi.domain.calendar.controller

import com.krstudy.kapi.domain.calendar.entity.Scalendar
import com.krstudy.kapi.domain.calendar.service.ScalendarService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/scalendar")
class RestCalendarController @Autowired constructor(
    private val scalendarService: ScalendarService
) {

    // Scalendar를 생성하는 엔드포인트
    @PostMapping
    fun createScalendar(@RequestBody scalendar: Scalendar): ResponseEntity<Scalendar> {
        println("scalendar: "+scalendar)
        val createdScalendar = scalendarService.createScalendar(scalendar)
        return ResponseEntity(createdScalendar, HttpStatus.CREATED)
    }

    // Scalendar를 ID로 조회하는 엔드포인트
    @GetMapping("/{id}")
    fun getScalendarById(@PathVariable id: Long): ResponseEntity<Scalendar> {
        val scalendar = scalendarService.getScalendarById(id)
        return if (scalendar != null) {
            ResponseEntity(scalendar, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    // Scalendar를 ID로 업데이트하는 엔드포인트
    @PutMapping("/{id}")
    fun updateScalendar(
        @PathVariable id: Long,
        @RequestBody updatedScalendar: Scalendar
    ): ResponseEntity<Scalendar> {
        val scalendar = scalendarService.updateScalendar(id, updatedScalendar)
        return if (scalendar != null) {
            ResponseEntity(scalendar, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    // Scalendar를 ID로 삭제하는 엔드포인트
    @DeleteMapping("/{id}")
    fun deleteScalendar(@PathVariable id: Long): ResponseEntity<Void> {
        scalendarService.deleteScalendar(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    // Scalendar의 조회 수를 증가시키는 엔드포인트
    @PatchMapping("/{id}/hit")
    fun increaseHit(@PathVariable id: Long): ResponseEntity<Void> {
        scalendarService.increaseHit(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}
