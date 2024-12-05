package com.krstudy.kapi.domain.payments.entity

import com.krstudy.kapi.domain.payments.status.IdempotencyStatus
import com.krstudy.kapi.global.jpa.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import java.time.LocalDateTime
import jakarta.persistence.EnumType

@Entity
@Table(name = "idempotency_keys")
class IdempotencyKey(
    @Column(nullable = false, unique = true)
    val key: String,

    @Column(nullable = false)
    val path: String,

    @Column(nullable = false)
    val method: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: IdempotencyStatus = IdempotencyStatus.PROCESSING,

    @Column
    var responseData: String? = null,

    @Column(nullable = false)
    val expiresAt: LocalDateTime = LocalDateTime.now().plusHours(24),

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
) : BaseEntity() {

    fun complete(responseData: String) {
        this.status = IdempotencyStatus.COMPLETED
        this.responseData = responseData
    }

    fun isExpired(): Boolean = LocalDateTime.now().isAfter(expiresAt)

    companion object {
        fun create(key: String, path: String, method: String): IdempotencyKey {
            return IdempotencyKey(
                key = key,
                path = path,
                method = method
            )
        }
    }
}
