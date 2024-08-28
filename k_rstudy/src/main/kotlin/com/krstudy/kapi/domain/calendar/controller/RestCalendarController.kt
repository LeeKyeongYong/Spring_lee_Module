package com.krstudy.kapi.domain.calendar.controller

import com.krstudy.kapi.domain.calendar.entity.Scalendar
import com.krstudy.kapi.domain.calendar.service.ScalendarService
import com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.domain.member.service.MemberService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/scalendar")
class RestCalendarController @Autowired constructor(
    private val scalendarService: ScalendarService,
    private val memberService: MemberService // Inject UserService
) {

    @GetMapping
    fun getAllScalendarEvents(): ResponseEntity<List<Map<String, Any?>>> {
        val events = scalendarService.getAllScalendarEvents()
        val response = events.map { event ->
            mapOf(
                "id" to event.id,
                "title" to event.title,
                "start" to event.startDay,
                "end" to event.endDay,
                "backgroundColor" to event.fcolor
            )
        }
        return ResponseEntity(response, HttpStatus.OK)
    }


    @PostMapping
    fun createScalendar(@RequestBody scalendar: Scalendar): ResponseEntity<Any> {

        return try {
            val authentication = SecurityContextHolder.getContext().authentication
            val username = (authentication.principal as UserDetails).username
            val author: Member = memberService.findByUserid(username)
                ?: throw UsernameNotFoundException("User not found with username: $username")
            scalendar.author = author
            val createdScalendar = scalendarService.createScalendar(scalendar)
            ResponseEntity(createdScalendar, HttpStatus.CREATED)
        } catch (e: UsernameNotFoundException) {
            e.printStackTrace()
            ResponseEntity(mapOf("error" to "User not found: ${e.message}"), HttpStatus.NOT_FOUND)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity(mapOf("error" to "An unexpected error occurred"), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("/{id}")
    fun getScalendarById(@PathVariable id: Long): ResponseEntity<Scalendar> {
        val scalendar = scalendarService.getScalendarById(id)
        return if (scalendar != null) {
            ResponseEntity(scalendar, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

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

    @DeleteMapping("/{id}")
    fun deleteScalendar(@PathVariable id: Long): ResponseEntity<Void> {
        scalendarService.deleteScalendar(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @PatchMapping("/{id}/hit")
    fun increaseHit(@PathVariable id: Long): ResponseEntity<Void> {
        scalendarService.increaseHit(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}