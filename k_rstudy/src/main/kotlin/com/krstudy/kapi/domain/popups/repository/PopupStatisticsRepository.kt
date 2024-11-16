package com.krstudy.kapi.domain.popups.repository

import com.krstudy.kapi.domain.popups.entity.PopupStatisticsEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate

interface PopupStatisticsRepository : JpaRepository<PopupStatisticsEntity, Long> {
    @Query("""
        SELECT ps FROM PopupStatisticsEntity ps 
        WHERE ps.popupId = :popupId 
        AND DATE(ps.createdAt) = :date
    """)
    fun findHourlyStatsByPopupAndDate(popupId: Long, date: LocalDate): List<PopupStatisticsEntity>

    @Query("""
        SELECT ps FROM PopupStatisticsEntity ps 
        WHERE ps.popupId = :popupId 
        GROUP BY ps.deviceType
    """)
    fun findDeviceStatsByPopup(popupId: Long): List<PopupStatisticsEntity>

    @Query("""
        SELECT ps FROM PopupStatisticsEntity ps 
        WHERE ps.popupId = :popupId
    """)
    fun findPopupStats(popupId: Long): PopupStatisticsEntity

    fun findByPopupId(popupId: Long): PopupStatisticsEntity?
}