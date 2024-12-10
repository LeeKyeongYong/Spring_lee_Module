package com.krstudy.kapi.domain.passwd.service

import com.krstudy.kapi.domain.member.service.MemberService
import com.krstudy.kapi.domain.passwd.dto.PasswordChangeAlertDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Service
@Transactional(readOnly = true)
class PasswordChangeAlertService(
    private val passwordChangeAlertRepository: PasswordChangeAlertRepository,
    private val memberService: MemberService,
    private val passwordChangeHistoryService: PasswordChangeHistoryService
) {

    fun checkPasswordChangeNeeded(memberId: Long): PasswordChangeAlertDto? {
        val member = memberService.getMemberByNo(memberId)
            ?: return null

        val lastPasswordChange = passwordChangeHistoryService
            .getPasswordChangeHistory(memberId)
            .maxByOrNull { it.changedAt }
            ?.changedAt
            ?: member.createDate

        val nextChangeDate = lastPasswordChange.plusMonths(3)
        val now = LocalDateTime.now()

        if (now.isAfter(nextChangeDate)) {
            return PasswordChangeAlertDto(
                memberId = memberId,
                lastPasswordChangeDate = lastPasswordChange,
                nextChangeDate = nextChangeDate,
                daysUntilChange = ChronoUnit.DAYS.between(now, nextChangeDate)
            )
        }

        return null
    }

    @Transactional
    fun autoChangePassword(memberId: Long): Boolean {
        return try {
            val member = memberService.getMemberByNo(memberId)
                ?: return false

            val newPassword = generateRandomPassword()

            memberService.changePassword(
                memberId = memberId,
                currentPassword = member.password,
                newPassword = newPassword,
                changeReason = "3개월 마다 변경으로 인한 사유",
                signature = null
            )

            // 알림 상태 업데이트
            passwordChangeAlertRepository.findByMemberId(memberId)?.let {
                it.isDismissed = true
                passwordChangeAlertRepository.save(it)
            }

            true
        } catch (e: Exception) {
            false
        }
    }

    private fun generateRandomPassword(): String {
        val chars = ('A'..'Z') + ('a'..'z') + ('0'..'9') + "!@#$%^&*"
        return (1..12).map { chars.random() }.joinToString("")
    }
}