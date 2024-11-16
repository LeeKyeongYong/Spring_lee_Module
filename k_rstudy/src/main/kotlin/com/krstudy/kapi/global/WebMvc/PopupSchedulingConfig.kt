package com.krstudy.kapi.global.WebMvc

import com.krstudy.kapi.domain.popups.entity.PopupEntity
import com.krstudy.kapi.domain.popups.entity.PopupScheduleEntity
import com.krstudy.kapi.domain.popups.enums.PopupStatus
import com.krstudy.kapi.domain.popups.enums.RepeatType
import com.krstudy.kapi.domain.popups.repository.PopupRepository
import com.krstudy.kapi.domain.popups.repository.PopupScheduleRepository
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import java.time.LocalDateTime

@Configuration
@EnableScheduling
class PopupSchedulingConfig(
    private val popupScheduleRepository: PopupScheduleRepository,
    private val popupRepository: PopupRepository
) {
    @Scheduled(cron = "0 * * * * *") // 매분 실행
    fun checkPopupSchedules() {
        val now = LocalDateTime.now()
        popupScheduleRepository.findActiveSchedules(now).forEach { schedule ->
            when (schedule.repeatType) {
                RepeatType.DAILY -> handleDailyRepeat(schedule, now)
                RepeatType.WEEKLY -> handleWeeklyRepeat(schedule, now)
                RepeatType.MONTHLY -> handleMonthlyRepeat(schedule, now)
            }
        }
    }

    private fun handleDailyRepeat(schedule: PopupScheduleEntity, now: LocalDateTime) {
        if (shouldActivatePopup(schedule, now)) {
            activatePopup(schedule.popup)
        }
    }

    private fun handleWeeklyRepeat(schedule: PopupScheduleEntity, now: LocalDateTime) {
        val currentDayOfWeek = now.dayOfWeek.value
        if (schedule.repeatDays?.split(",")?.map { it.toInt() }
                ?.contains(currentDayOfWeek) == true) {
            activatePopup(schedule.popup)
        }
    }

    private fun handleMonthlyRepeat(schedule: PopupScheduleEntity, now: LocalDateTime) {
        if (schedule.repeatMonthDay == now.dayOfMonth) {
            activatePopup(schedule.popup)
        }
    }

    private fun shouldActivatePopup(schedule: PopupScheduleEntity, now: LocalDateTime): Boolean {
        val scheduleTime = now.withHour(schedule.startTime.hour)
            .withMinute(schedule.startTime.minute)
        return now.isEqual(scheduleTime)
    }

    private fun activatePopup(popup: PopupEntity) {
        popup.status = PopupStatus.ACTIVE
        popupRepository.save(popup)
    }
}