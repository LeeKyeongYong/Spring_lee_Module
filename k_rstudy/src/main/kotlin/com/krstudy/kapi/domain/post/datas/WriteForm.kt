package com.krstudy.kapi.domain.post.datas

import jakarta.validation.constraints.NotBlank

data class WriteForm(
//    @field:NotBlank
//    var body: String? = null
    @field:NotBlank val title: String = "",
    @field:NotBlank val body: String = "",
    val isPublished: Boolean = false
)

/*
    data class WriteForm(
        @field:NotBlank val title: String,
        @field:NotBlank val body: String,
        val isPublished: Boolean
    )

 */