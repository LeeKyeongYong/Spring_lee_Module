package com.krstudy.kapi.domain.member.datas

import com.krstudy.kapi.domain.oauth2.entity.Social

data class RegistrationData(
    val userid: String,
    val username: String,
    val password: String,
    val userEmail: String,
    val imageType: String? = null,
    val imageBytes: ByteArray? = null,
    val social: Social? = null, // 추가된 social 필드
    val additionalFields: Map<String, Any> = emptyMap() // 추가 필드를 위한 Map
)