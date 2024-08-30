package com.krstudy.kapi.domain.excel.entity

import java.io.Serializable

data class PostDetailsId(
    val postId: Long? = null,
    val commentId: Long? = null
) : Serializable
