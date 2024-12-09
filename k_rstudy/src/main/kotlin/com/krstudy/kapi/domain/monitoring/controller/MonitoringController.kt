package com.krstudy.kapi.domain.monitoring.controller

import com.krstudy.kapi.domain.monitoring.service.SystemMonitorService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@Controller
@RequestMapping("/monitoring")
class MonitoringController(
    private val systemMonitorService: SystemMonitorService
) {
    @GetMapping
    fun showMonitoringDashboard(model: Model): String {
        val recentMetrics = systemMonitorService.getMetricsByDateRange(
            LocalDateTime.now().minusHours(1),
            LocalDateTime.now()
        ).body ?: listOf()  // ResponseEntity에서 body 추출
        model.addAttribute("metrics", recentMetrics)
        return "domain/monitoring/dashboard"
    }

    @GetMapping("/history")
    fun showMetricsHistory(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) startDate: LocalDateTime,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) endDate: LocalDateTime,
        model: Model
    ): String {
        val metrics = systemMonitorService.getMetricsByDateRange(startDate, endDate).body ?: listOf()
        model.addAttribute("metrics", metrics)
        return "domain/monitoring/history"
    }
}