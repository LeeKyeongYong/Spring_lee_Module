package com.krstudy.kapi.global.jpa

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
    private var createDate: LocalDateTime? = null

    @LastModifiedDate
    private var modifyDate: LocalDateTime? = null

    // Getters and Setters
    fun getCreateDate(): LocalDateTime? = createDate
    fun setCreateDate(createDate: LocalDateTime?) { this.createDate = createDate }

    fun getModifyDate(): LocalDateTime? = modifyDate
    fun setModifyDate(modifyDate: LocalDateTime?) { this.modifyDate = modifyDate }
}
