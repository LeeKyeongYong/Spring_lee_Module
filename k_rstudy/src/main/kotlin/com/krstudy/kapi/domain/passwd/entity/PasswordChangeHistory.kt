package com.krstudy.kapi.domain.passwd.entity

import com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.global.jpa.BaseEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class PasswordChangeHistory(


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member,

    @Column(nullable = false)
    val changeReason: String,

    @Column(nullable = false)
    val changedAt: LocalDateTime = LocalDateTime.now(),

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    val signature: ByteArray? = null,

    @Column
    val signatureType: String? = null
):BaseEntity(){}