package com.krstudy.kapi.domain.post.datas

import jakarta.validation.constraints.NotBlank

data class WriteForm(
    @field:NotBlank
    var body: String? = null
)