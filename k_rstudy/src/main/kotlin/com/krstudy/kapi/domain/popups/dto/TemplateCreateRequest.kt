package com.krstudy.kapi.domain.popups.dto

data class TemplateCreateRequest(
    val name: String,
    val content: String,
    val width: Int,
    val height: Int,
    val backgroundColor: String?,
    val borderStyle: String?,
    val isDefault: Boolean = false
)