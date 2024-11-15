package com.krstudy.kapi.domain.banners.service

import com.krstudy.kapi.domain.banners.dto.BannerCreateRequest
import com.krstudy.kapi.domain.banners.dto.BannerResponse
import com.krstudy.kapi.domain.banners.entity.BannerEntity
import com.krstudy.kapi.domain.banners.repository.BannerRepository
import com.krstudy.kapi.domain.member.repository.MemberRepository
import com.krstudy.kapi.domain.uploads.exception.FileUploadException
import com.krstudy.kapi.domain.uploads.service.FileServiceImpl;
import com.krstudy.kapi.global.exception.BannerCreationException
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class BannerService(
    private val bannerRepository: BannerRepository,
    private val fileService: FileServiceImpl,
    private val memberRepository: MemberRepository
) {
    @Transactional
    fun createBanner(request: BannerCreateRequest, imageFile: MultipartFile, userId: String): BannerResponse {
        try {
            val creator = memberRepository.findByUserid(userId)
                ?: throw EntityNotFoundException("User not found")

            val bannerImage = fileService.uploadFiles(arrayOf(imageFile), userId).firstOrNull()
                ?: throw FileUploadException("Failed to upload banner image")

            // DateTimeFormatter 정의
            val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

            // String을 LocalDateTime으로 변환
            val startDateTime = try {
                LocalDateTime.parse(request.startDate, formatter)
            } catch (e: Exception) {
                throw IllegalArgumentException("Invalid start date format: ${request.startDate}")
            }

            val endDateTime = try {
                LocalDateTime.parse(request.endDate, formatter)
            } catch (e: Exception) {
                throw IllegalArgumentException("Invalid end date format: ${request.endDate}")
            }

            // 날짜 유효성 검사
            if (endDateTime.isBefore(startDateTime)) {
                throw IllegalArgumentException("End date must be after start date")
            }

            val banner = BannerEntity(
                title = request.title,
                description = request.description,
                linkUrl = request.linkUrl,
                displayOrder = request.displayOrder,
                bannerImage = bannerImage,
                creator = creator,
                startDate = startDateTime,
                endDate = endDateTime
            )

            val savedBanner = bannerRepository.save(banner)
            return savedBanner.toResponse()
        } catch (e: Exception) {
            throw BannerCreationException("Failed to create banner: ${e.message}", e)
        }
    }

    fun getActiveBanners(): List<BannerResponse> {
        return bannerRepository.findActiveBanners()
            .map { it.toResponse() }
    }

    fun getUserBanners(userId: String): List<BannerResponse> {
        return bannerRepository.findByCreatorId(userId)
            .map { it.toResponse() }
    }

    private fun BannerEntity.toResponse() = BannerResponse(
        id = id,
        title = title,
        description = description,
        linkUrl = linkUrl,  // null 허용
        displayOrder = displayOrder,
        status = status,
        imageUrl = "/api/files/${bannerImage.id}",
        creatorName = creator.username,  // null 허용
        startDate = startDate,
        endDate = endDate,
        createDate = getCreateDate() ?: LocalDateTime.now()  // null 안전 처리
    )
}