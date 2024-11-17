package com.krstudy.kapi.domain.popups.dto

data class TemplateCreateRequest(
    val name: String,
    val content: String? = null,
    val width: Int,
    val height: Int,
    val backgroundColor: String? = null,
    val borderStyle: String? = null,
    val isDefault: Boolean = false
)