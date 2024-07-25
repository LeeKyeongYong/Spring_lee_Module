package com.krstudy.kapi.com.krstudy.kapi.global.jpa

import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import lombok.Getter
import lombok.ToString
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime


@MappedSuperclass
@Getter
@EntityListeners(AuditingEntityListener::class)
@ToString(callSuper = true)
open class BaseEntity : IdEntity() {
    @CreatedDate
    private val createDate: LocalDateTime? = null

    @LastModifiedDate
    private val modifyDate: LocalDateTime? = null
}