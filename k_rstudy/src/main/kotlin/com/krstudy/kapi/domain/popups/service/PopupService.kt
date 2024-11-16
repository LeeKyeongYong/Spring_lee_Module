package com.krstudy.kapi.domain.popups.service

import com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.domain.member.repository.MemberRepository
import com.krstudy.kapi.domain.popups.dto.*
import com.krstudy.kapi.domain.popups.entity.DeviceType
import com.krstudy.kapi.domain.popups.entity.PopupEntity
import com.krstudy.kapi.domain.popups.entity.PopupHistoryEntity
import com.krstudy.kapi.domain.popups.entity.PopupTemplateEntity
import com.krstudy.kapi.domain.popups.enums.PopupStatus
import com.krstudy.kapi.domain.popups.exception.PopupCreationException
import com.krstudy.kapi.domain.popups.factory.PopupFactory
import com.krstudy.kapi.domain.popups.repository.*
import com.krstudy.kapi.domain.uploads.service.FileServiceImpl
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime


@Service
class PopupService(
    private val popupRepository: PopupRepository,
    private val fileService: FileServiceImpl,
    private val memberRepository: MemberRepository,
    private val popupFactory: PopupFactory,
    private val popupHistoryRepository: PopupHistoryRepository,
    private val popupTemplateRepository: PopupTemplateRepository,
    private val popupStatisticsRepository: PopupStatisticsRepository,
    private val popupScheduleRepository: PopupScheduleRepository
) {
    /**
     * 팝업 요청 검증
     */
    private fun validatePopupRequest(request: PopupCreateRequest) {
        require(request.title.isNotBlank()) { "제목은 필수입니다." }
        require(request.width in 100..2000) { "팝업 너비는 100px에서 2000px 사이여야 합니다." }
        require(request.height in 100..2000) { "팝업 높이는 100px에서 2000px 사이여야 합니다." }
        require(request.startDateTime < request.endDateTime) { "시작일시는 종료일시보다 이전이어야 합니다." }
    }

    /**
     * 이미지 파일 검증
     */
    private fun validateImageFile(file: MultipartFile) {
        require(file.contentType?.startsWith("image/") == true) { "이미지 파일만 업로드 가능합니다." }
        require(file.size <= 5 * 1024 * 1024) { "이미지 크기는 5MB를 초과할 수 없습니다." }
    }


    /**
     * 활성화된 팝업 조회
     */
    @Transactional(readOnly = true)
    fun getActivePopups(deviceType: DeviceType, page: String? = null): List<PopupResponse> {
        val now = LocalDateTime.now()
        val popups = if (page != null) {
            popupRepository.findPopupsByPage(page, now)
        } else {
            popupRepository.findActivePopups(now, deviceType)
        }
        return popups.map { PopupResponse.from(it) }
    }

    /**
     * 사용자 권한 기반 활성화된 팝업 조회
     */
    fun getActivePopups(deviceType: DeviceType, page: String?, user: Member?): List<PopupResponse> {
        val popups = popupRepository.findActivePopups(LocalDateTime.now(), deviceType)
        return popups.filter { popup ->
            popup.targetRoles.isEmpty() ||
                    user?.getAuthoritiesAsStringList()?.any { it in popup.targetRoles } == true
        }.map { PopupResponse.from(it) }
    }

    /**
     * 모든 팝업 조회
     */
    fun getAllPopups(): List<PopupResponse> {
        return popupRepository.findAll().map { PopupResponse.from(it) }
    }

    /**
     * 팝업 조회수 증가
     */
    @Transactional
    fun incrementViewCount(popupId: Long) {
        val popup = popupRepository.findById(popupId).orElseThrow {
            EntityNotFoundException("Popup not found")
        }
        popup.viewCount++
    }

    /**
     * 팝업 클릭수 증가
     */
    @Transactional
    fun incrementClickCount(popupId: Long) {
        val popup = popupRepository.findById(popupId).orElseThrow {
            EntityNotFoundException("Popup not found")
        }
        popup.clickCount++
    }

    /**
     * 팝업 생성
     */
    @Transactional
    fun createPopup(
        request: PopupCreateRequest,
        image: MultipartFile?,
        userId: String
    ): PopupResponse {
        try {
            validatePopupRequest(request)
            image?.let { validateImageFile(it) }

            val creator = memberRepository.findByUserid(userId)
                ?: throw EntityNotFoundException("User not found")

            val popupImage = image?.let {
                fileService.uploadFiles(arrayOf(it), userId).firstOrNull()
            }

            val popup = popupFactory.createPopupEntity(request, popupImage, creator)
            val savedPopup = popupRepository.save(popup)
            return PopupResponse.from(savedPopup)
        } catch (e: Exception) {
            throw PopupCreationException("팝업 생성 실패: ${e.message}", e)
        }
    }

    /**
     * 팝업 삭제
     */
    @Transactional
    fun deletePopup(id: Long) {
        val popup = popupRepository.findById(id).orElseThrow {
            EntityNotFoundException("팝업을 찾을 수 없습니다: $id")
        }
        popup.status = PopupStatus.DELETED
        popupRepository.save(popup)
    }

    /**
     * 팝업 상태 업데이트
     */
    @Transactional
    fun updatePopupStatus(id: Long, status: String): PopupResponse {
        val popup = popupRepository.findById(id).orElseThrow {
            EntityNotFoundException("팝업을 찾을 수 없습니다: $id")
        }
        popup.status = PopupStatus.valueOf(status.uppercase())
        return PopupResponse.from(popupRepository.save(popup))
    }

    /**
     * 팝업 수정
     */
    @Transactional
    fun updatePopup(
        id: Long,
        request: PopupCreateRequest,
        image: MultipartFile?,
        userId: String
    ): PopupResponse {
        try {
            validatePopupRequest(request)
            image?.let { validateImageFile(it) }

            val popup = popupRepository.findById(id).orElseThrow {
                EntityNotFoundException("팝업을 찾을 수 없습니다: $id")
            }

            val creator = memberRepository.findByUserid(userId)
                ?: throw EntityNotFoundException("사용자를 찾을 수 없습니다")

            val popupImage = image?.let {
                fileService.uploadFiles(arrayOf(it), userId).firstOrNull()
            } ?: popup.image

            // 팝업 정보 업데이트
            popup.apply {
                title = request.title
                content = request.content
                startDateTime = request.startDateTime
                endDateTime = request.endDateTime
                priority = request.priority
                width = request.width
                height = request.height
                positionX = request.positionX
                positionY = request.positionY
                this.image = popupImage
                linkUrl = request.linkUrl
                altText = request.altText
                target = request.target
                deviceType = request.deviceType
                cookieExpireDays = request.cookieExpireDays
                hideForToday = request.hideForToday
                hideForWeek = request.hideForWeek
                backgroundColor = request.backgroundColor
                borderStyle = request.borderStyle
                shadowEffect = request.shadowEffect
                animationType = request.animationType
                displayPages = request.displayPages.toSet()
                targetRoles = request.targetRoles.toSet()
                maxDisplayCount = request.maxDisplayCount
            }

            return PopupResponse.from(popupRepository.save(popup))
        } catch (e: Exception) {
            throw PopupCreationException("팝업 수정 실패: ${e.message}", e)
        }
    }

    /**
     * 팝업 상세 조회
     */
    @Transactional(readOnly = true)
    fun getPopup(id: Long): PopupResponse {
        val popup = popupRepository.findById(id).orElseThrow {
            EntityNotFoundException("팝업을 찾을 수 없습니다: $id")
        }
        return PopupResponse.from(popup)
    }

    /**
     * 팝업 미리보기
     */
    fun previewPopup(id: Long): PopupPreviewResponse {
        val popup = popupRepository.findById(id).orElseThrow {
            EntityNotFoundException("Popup not found")
        }
        return PopupPreviewResponse.from(popup)
    }

    /**
     * 팝업 상태 변경
     */
    @Transactional
    fun changePopupStatus(id: Long, status: PopupStatus, userId: String) {
        val popup = popupRepository.findById(id).orElseThrow {
            EntityNotFoundException("Popup not found")
        }
        val editor = memberRepository.findByUserid(userId)
            ?: throw EntityNotFoundException("User not found")

        popup.status = status
        savePopupHistory(popup, editor, "STATUS_CHANGE",
            mapOf("oldStatus" to popup.status, "newStatus" to status))
    }

    /**
     * 팝업 복제
     */
    @Transactional
    fun clonePopup(id: Long, userId: String): PopupResponse {
        val originalPopup = popupRepository.findById(id).orElseThrow {
            EntityNotFoundException("Popup not found")
        }
        val creator = memberRepository.findByUserid(userId)
            ?: throw EntityNotFoundException("User not found")

        val clonedPopup = originalPopup.copy(
            title = "${originalPopup.title} (복사본)",
            status = PopupStatus.INACTIVE,
            creator = creator
        )

        val savedPopup = popupRepository.save(clonedPopup)
        savePopupHistory(savedPopup, creator, "CLONE",
            mapOf("originalId" to id))

        return savedPopup.toResponse()
    }


    /**
     * 템플릿 저장
     */
    @Transactional
    fun saveTemplate(request: TemplateCreateRequest, userId: String): TemplateResponse {
        val creator = memberRepository.findByUserid(userId)
            ?: throw EntityNotFoundException("User not found")

        val template = PopupTemplateEntity(
            name = request.name,
            content = request.content,
            width = request.width,
            height = request.height,
            backgroundColor = request.backgroundColor,
            borderStyle = request.borderStyle,
            creator = creator,
            isDefault = request.isDefault
        )

        return popupTemplateRepository.save(template).toResponse()
    }

    /**
     * 통계 업데이트
     */
    @Transactional
    fun updateStatistics(id: Long, statsType: String, deviceType: String?) {
        val statistics = popupStatisticsRepository.findByPopupId(id)
            ?: throw EntityNotFoundException("Statistics not found")

        when (statsType) {
            "VIEW" -> {
                statistics.viewCount++
                deviceType?.let {
                    statistics.deviceStats.merge(it, 1L, Long::plus)
                }
            }
            "CLICK" -> statistics.clickCount++
            "CLOSE" -> {
                statistics.closeCount++
                statistics.closeTypeStats.merge(deviceType ?: "NORMAL", 1L, Long::plus)
            }
        }
    }

    /**
     * CTR 계산
     */
    fun calculateCTR(id: Long): Double {
        val statistics = popupStatisticsRepository.findByPopupId(id)
            ?: throw EntityNotFoundException("Statistics not found")

        return if (statistics.viewCount > 0) {
            (statistics.clickCount.toDouble() / statistics.viewCount) * 100
        } else 0.0
    }

    private fun savePopupHistory(
        popup: PopupEntity,
        editor: Member,
        action: String,
        details: Map<String, Any>
    ) {
        val historyEntity = PopupHistoryEntity(
            popup = popup,
            editor = editor,
            action = action,
            changeDetails = objectMapper.writeValueAsString(details)
        )
        popupHistoryRepository.save(historyEntity)
    }

}