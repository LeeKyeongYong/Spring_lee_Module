package com.krstudy.kapi.global.jpa

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import java.io.Serializable

@MappedSuperclass
abstract class IdEntity : Serializable {
    @Schema(description = "게시물 및 사용자 정보 아이디(공용)", example = "id")
    @Id
    @GeneratedValue(strategy = IDENTITY)
    open val id: Long = 0L

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IdEntity

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "IdEntity(id=$id)"
    }
}

