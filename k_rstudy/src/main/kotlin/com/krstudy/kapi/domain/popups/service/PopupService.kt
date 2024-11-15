package com.krstudy.kapi.domain.popups.service

import com.krstudy.kapi.domain.member.repository.MemberRepository
import com.krstudy.kapi.domain.popups.dto.PopupCreateRequest
import com.krstudy.kapi.domain.popups.dto.PopupResponse
import com.krstudy.kapi.domain.popups.entity.DeviceType
import com.krstudy.kapi.domain.popups.repository.PopupRepository
import com.krstudy.kapi.domain.uploads.service.FileServiceImpl
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

/**
 * 팝업 서비스
 * 팝업의 생성, 수정, 조회, 삭제 등을 처리
 */
@Service
class PopupService(
    private val popupRepository: PopupRepository,
    private val fileService: FileServiceImpl,
    private val memberRepository: MemberRepository
) {
    /**
     * 팝업 생성
     * @param request 팝업 생성 요청 정보
     * @param image 팝업 이미지 (선택)
     * @param userId 생성자 ID
     * @return 생성된 팝업 정보
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

            val popup = createPopupEntity(request, popupImage, creator)
            val savedPopup = popupRepository.save(popup)
            return savedPopup.toResponse()
        } catch (e: Exception) {
            throw PopupCreationException("Failed to create popup: ${e.message}", e)
        }
    }

    /**
     * 활성화된 팝업 조회
     * @param deviceType 디바이스 타입
     * @param page 현재 페이지 경로
     * @return 활성화된 팝업 목록
     */
    @Transactional(readOnly = true)
    fun getActivePopups(deviceType: DeviceType, page: String? = null): List<PopupResponse> {
        val now = LocalDateTime.now()
        val popups = if (page != null) {
            popupRepository.findPopupsByPage(page, now)
        } else {
            popupRepository.findActivePopups(now, deviceType)
        }
        return popups.map { it.toResponse() }
    }

    /**
     * 팝업 조회수 증가
     * @param popupId 팝업 ID
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
     * @param popupId 팝업 ID
     */
    @Transactional
    fun incrementClickCount(popupId: Long) {
        val popup = popupRepository.findById(popupId).orElseThrow {
            EntityNotFoundException("Popup not found")
        }
        popup.clickCount++
    }

}