package com.krstudy.kapi.domain.passwd.entity

import com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.global.jpa.BaseEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class MemberSignature(

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)  // Ensure this is not nullable
    val member: Member,

    @Lob
    @Column(columnDefinition = "LONGBLOB", nullable = false)  // Ensure signature is not nullable
    val signature: ByteArray,

    @Column(nullable = false)  // Make signatureType required
    val signatureType: String,
): BaseEntity(){}
