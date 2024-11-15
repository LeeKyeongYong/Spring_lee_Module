package com.krstudy.kapi.domain.banners.service

import com.krstudy.kapi.domain.banners.dto.BannerCreateRequest
import com.krstudy.kapi.domain.banners.dto.BannerResponse
import com.krstudy.kapi.domain.banners.entity.BannerEntity
import com.krstudy.kapi.domain.banners.repository.BannerRepository
import com.krstudy.kapi.domain.member.repository.MemberRepository
import com.krstudy.kapi.domain.uploads.exception.FileUploadException
import com.krstudy.kapi.domain.uploads.service.FileServiceImpl;
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

@Service
class BannerService(
    private val bannerRepository: BannerRepository,
    private val fileService: FileServiceImpl,
    private val memberRepository: MemberRepository
) {
    @Transactional
    fun createBanner(request: BannerCreateRequest, imageFile: MultipartFile, userId: String): BannerResponse {
        val creator = memberRepository.findByUserid(userId)
            ?: throw EntityNotFoundException("User not found")

        val bannerImage = fileService.uploadFiles(arrayOf(imageFile), userId).firstOrNull()
            ?: throw FileUploadException("Failed to upload banner image")

        val banner = BannerEntity(
            title = request.title,
            description = request.description,
            linkUrl = request.linkUrl,
            displayOrder = request.displayOrder,
            bannerImage = bannerImage,
            creator = creator,
            startDate = request.startDate,
            endDate = request.endDate
        )

        val savedBanner = bannerRepository.save(banner)
        return savedBanner.toResponse()
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