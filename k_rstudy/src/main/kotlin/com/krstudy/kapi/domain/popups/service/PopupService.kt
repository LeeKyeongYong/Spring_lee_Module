package com.krstudy.kapi.domain.popups.service

import com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.domain.member.repository.MemberRepository
import com.krstudy.kapi.domain.popups.dto.PopupCreateRequest
import com.krstudy.kapi.domain.popups.dto.PopupResponse
import com.krstudy.kapi.domain.popups.entity.DeviceType
import com.krstudy.kapi.domain.popups.entity.PopupEntity
import com.krstudy.kapi.domain.popups.enums.PopupStatus
import com.krstudy.kapi.domain.popups.exception.PopupCreationException
import com.krstudy.kapi.domain.popups.factory.PopupFactory
import com.krstudy.kapi.domain.popups.repository.PopupRepository
import com.krstudy.kapi.domain.uploads.entity.FileEntity
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
    private val popupFactory: PopupFactory
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

}