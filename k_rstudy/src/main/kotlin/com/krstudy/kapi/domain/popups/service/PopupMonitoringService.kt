package com.krstudy.kapi.domain.popups.service

@Service
class PopupMonitoringService {
    // 성능 모니터링
    fun trackPopupPerformance(popupId: Long)

    // 이상 징후 감지
    fun detectAnomalies()

    // 관리자 알림
    fun sendAlert(alertType: AlertType, message: String)
}