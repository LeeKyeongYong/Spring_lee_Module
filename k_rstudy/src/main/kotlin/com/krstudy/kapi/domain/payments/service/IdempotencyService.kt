package com.krstudy.kapi.domain.payments.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.krstudy.kapi.domain.payments.entity.IdempotencyKey
import com.krstudy.kapi.domain.payments.repository.IdempotencyKeyRepository
import com.krstudy.kapi.domain.payments.status.IdempotencyStatus
import com.krstudy.kapi.global.exception.GlobalException
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class IdempotencyService(
    private val idempotencyKeyRepository: IdempotencyKeyRepository,
    private val objectMapper: ObjectMapper
) {
    @Transactional
    fun <T> processWithIdempotency(
        idempotencyKey: String,
        path: String,
        method: String,
        processor: () -> ResponseEntity<T>
    ): ResponseEntity<T> {
        // 기존 키 확인
        idempotencyKeyRepository.findByKey(idempotencyKey)?.let { existingKey ->
            if (existingKey.status == IdempotencyStatus.PROCESSING) {
                throw GlobalException("409-1", "동일한 요청이 처리 중입니다.")
            }

            if (existingKey.path != path || existingKey.method != method) {
                throw GlobalException("422-1", "동일한 멱등성 키로 다른 요청을 시도할 수 없습니다.")
            }

            if (existingKey.isExpired()) {
                idempotencyKeyRepository.delete(existingKey)
            } else {
                @Suppress("UNCHECKED_CAST")
                return objectMapper.readValue(existingKey.responseData, ResponseEntity::class.java) as ResponseEntity<T>
            }
        }

        val newKey = IdempotencyKey.create(idempotencyKey, path, method)
        idempotencyKeyRepository.save(newKey)

        return try {
            val response = processor()
            newKey.complete(objectMapper.writeValueAsString(response))
            idempotencyKeyRepository.save(newKey)
            response
        } catch (e: Exception) {
            newKey.status = IdempotencyStatus.FAILED
            idempotencyKeyRepository.save(newKey)
            throw e
        }
    }
}

// PaymentService.kt의 retryWithTimeout 수정 (코루틴 제거)
private fun <T> retryWithTimeout(
    maxAttempts: Int,
    block: () -> T
): T {
    var lastException: Exception? = null

    repeat(maxAttempts) { attempt ->
        try {
            return block()
        } catch (e: Exception) {
            lastException = e
            if (attempt < maxAttempts - 1) {
                Thread.sleep(attempt * 1000L) // 지수 백오프
            }
        }
    }

    throw lastException ?: RuntimeException("Operation failed after $maxAttempts attempts")
}