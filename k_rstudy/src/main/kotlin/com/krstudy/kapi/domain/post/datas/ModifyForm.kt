package com.krstudy.kapi.domain.post.datas

import jakarta.validation.constraints.NotBlank

data class ModifyForm(
    @field:NotBlank
    var body: String? = null
)