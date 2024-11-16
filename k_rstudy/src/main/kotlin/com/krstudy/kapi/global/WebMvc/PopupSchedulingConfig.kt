package com.krstudy.kapi.global.WebMvc

@Configuration
@EnableScheduling
class PopupSchedulingConfig {
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
}