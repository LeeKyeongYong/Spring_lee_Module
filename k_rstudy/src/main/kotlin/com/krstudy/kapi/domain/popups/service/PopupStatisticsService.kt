package com.krstudy.kapi.domain.popups.service

@Service
class PopupStatisticsService {
    // 시간대별 통계
    fun getHourlyStats(popupId: Long, date: LocalDate): Map<Int, PopupStatistics>

    // 디바이스별 전환율(CTR) 분석
    fun getDeviceCTRStats(popupId: Long): Map<DeviceType, Double>

    // A/B 테스트 지원
    fun getABTestResults(popupIdA: Long, popupIdB: Long): ABTestResult
}