package com.krstudy.kapi.domain.popups.service

import com.krstudy.kapi.domain.popups.dto.ABTestResult
import com.krstudy.kapi.domain.popups.dto.PopupStatistics
import com.krstudy.kapi.domain.popups.dto.PopupTestStats
import com.krstudy.kapi.domain.popups.entity.DeviceType
import com.krstudy.kapi.domain.popups.repository.PopupStatisticsRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import kotlin.math.sqrt

@Service
class PopupStatisticsService(
    private val statisticsRepository: PopupStatisticsRepository
) {
    // 시간대별 통계
    fun getHourlyStats(popupId: Long, date: LocalDate): Map<Int, PopupStatistics> {
        val stats = statisticsRepository.findHourlyStatsByPopupAndDate(popupId, date)
        return stats.groupBy { it.hour }
            .mapValues { (_, records) ->
                PopupStatistics(
                    viewCount = records.sumOf { it.viewCount },
                    clickCount = records.sumOf { it.clickCount },
                    ctr = calculateCTR(records.sumOf { it.clickCount }, records.sumOf { it.viewCount }),
                    averageViewDuration = records.map { it.viewDuration }.average()
                )
            }
    }

    // 디바이스별 전환율(CTR) 분석
    fun getDeviceCTRStats(popupId: Long): Map<DeviceType, Double> {
        val deviceStats = statisticsRepository.findDeviceStatsByPopup(popupId)
        return deviceStats.associate { stat ->
            stat.deviceType to calculateCTR(stat.clickCount, stat.viewCount)
        }
    }

    // A/B 테스트 결과 분석
    fun getABTestResults(popupIdA: Long, popupIdB: Long): ABTestResult {
        val statsA = getPopupTestStats(popupIdA)
        val statsB = getPopupTestStats(popupIdB)

        val confidenceLevel = calculateConfidenceLevel(statsA, statsB)
        val winner = determineWinner(statsA, statsB, confidenceLevel)

        return ABTestResult(
            popupA = statsA,
            popupB = statsB,
            winner = winner,
            confidenceLevel = confidenceLevel
        )
    }

    private fun getPopupTestStats(popupId: Long): PopupTestStats {
        val stats = statisticsRepository.findPopupStats(popupId)
        return PopupTestStats(
            popupId = popupId,
            impressions = stats.viewCount,
            clicks = stats.clickCount,
            ctr = calculateCTR(stats.clickCount, stats.viewCount),
            conversionRate = calculateConversionRate(stats.conversionCount, stats.viewCount)
        )
    }

    private fun calculateCTR(clicks: Long, views: Long): Double {
        return if (views > 0) (clicks.toDouble() / views) * 100 else 0.0
    }

    private fun calculateConversionRate(conversions: Long, views: Long): Double {
        return if (views > 0) (conversions.toDouble() / views) * 100 else 0.0
    }

    private fun calculateConfidenceLevel(statsA: PopupTestStats, statsB: PopupTestStats): Double {
        // Z-test for proportions
        val pA = statsA.clicks.toDouble() / statsA.impressions
        val pB = statsB.clicks.toDouble() / statsB.impressions
        val se = sqrt(
            (pA * (1 - pA) / statsA.impressions) +
                    (pB * (1 - pB) / statsB.impressions)
        )
        val z = (pA - pB) / se
        return calculatePValue(z)
    }

    private fun calculatePValue(z: Double): Double {
        // 간단한 p-value 계산 (정규분포 가정)
        return 1 - (0.5 * (1 + erf(z / sqrt(2.0))))
    }

    private fun erf(x: Double): Double {
        // Error function 근사값 계산
        val a1 = 0.254829592
        val a2 = -0.284496736
        val a3 = 1.421413741
        val a4 = -1.453152027
        val a5 = 1.061405429
        val p = 0.3275911

        val sign = if (x >= 0) 1 else -1
        val t = 1.0 / (1.0 + p * kotlin.math.abs(x))
        val y = 1.0 - (((((a5 * t + a4) * t) + a3) * t + a2) * t + a1) * t * kotlin.math.exp(-x * x)

        return sign * y
    }

    private fun determineWinner(
        statsA: PopupTestStats,
        statsB: PopupTestStats,
        confidenceLevel: Double
    ): String? {
        return when {
            confidenceLevel >= 0.95 -> {
                if (statsA.ctr > statsB.ctr) "A" else "B"
            }
            else -> null // 통계적으로 유의미한 차이가 없음
        }
    }
}