package com.krstudy.kapi.domain.passwd.service

import com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.domain.passwd.entity.MemberSignature
import com.krstudy.kapi.domain.passwd.entity.PasswordChangeHistory
import com.krstudy.kapi.domain.passwd.repository.MemberSignatureRepository
import com.krstudy.kapi.domain.passwd.repository.PasswordChangeHistoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
@Transactional(readOnly = true)
class PasswordChangeHistoryService(
    private val passwordChangeHistoryRepository: PasswordChangeHistoryRepository,
    private val memberSignatureRepository: MemberSignatureRepository
) {

    @Transactional
    fun savePasswordChangeHistory(
        member: Member,
        changeReason: String,
        signature: MultipartFile?
    ): PasswordChangeHistory {
        var signatureBytes: ByteArray? = null
        var signatureType: String = ""

        if (signature != null && !signature.isEmpty) {
            signatureBytes = signature.bytes
            signatureType = signature.contentType ?: ""

            // Save signature
            val memberSignature = MemberSignature(
                member = member,
                signature = signatureBytes,
                signatureType = signatureType
            )
            memberSignatureRepository.save(memberSignature)
        }

        val history = PasswordChangeHistory(
            member = member,
            changeReason = changeReason,
            signature = signatureBytes,
            signatureType = signatureType
        )

        return passwordChangeHistoryRepository.save(history)
    }

    fun getPasswordChangeHistory(memberId: Long): List<PasswordChangeHistory> {
        return passwordChangeHistoryRepository.findByMemberIdOrderByChangedAtDesc(memberId)
    }

    fun getMemberSignature(memberId: Long): MemberSignature? {
        return memberSignatureRepository.findByMemberId(memberId)
    }
}