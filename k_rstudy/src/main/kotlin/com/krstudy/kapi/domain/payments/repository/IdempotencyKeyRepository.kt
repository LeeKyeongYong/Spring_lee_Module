package com.krstudy.kapi.domain.payments.repository

import com.krstudy.kapi.domain.payments.entity.IdempotencyKey
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface IdempotencyKeyRepository : JpaRepository<IdempotencyKey, Long> {
    fun findByKey(key: String): IdempotencyKey?
    fun existsByKey(key: String): Boolean
}