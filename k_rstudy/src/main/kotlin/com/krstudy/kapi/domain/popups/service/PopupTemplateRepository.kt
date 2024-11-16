package com.krstudy.kapi.domain.popups.repository

import com.krstudy.kapi.domain.popups.entity.PopupTemplateEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PopupTemplateRepository : JpaRepository<PopupTemplateEntity, Long> {
    fun findByIsDefaultTrue(): List<PopupTemplateEntity>
}