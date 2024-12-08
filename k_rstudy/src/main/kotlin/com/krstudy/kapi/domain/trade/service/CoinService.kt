package com.krstudy.kapi.domain.trade.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.krstudy.kapi.com.krstudy.kapi.domain.trade.client.MarketDataClient
import com.krstudy.kapi.domain.trade.dto.CoinDto
import com.krstudy.kapi.domain.trade.dto.HogaDto
import com.krstudy.kapi.domain.trade.repository.CoinRepository
import com.krstudy.kapi.global.exception.GlobalException
import com.krstudy.kapi.global.exception.MessageCode
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory

@Service
class CoinService(
    private val coinRepository: CoinRepository,
    private val redisTemplate: RedisTemplate<String, Any>,
    private val marketDataClient: MarketDataClient
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val objectMapper = jacksonObjectMapper()

    @Transactional(readOnly = true)
    suspend fun getAllCoins(): Flow<CoinDto> = withContext(Dispatchers.IO) {
        flow {
            try {
                coinRepository.findAllByOrderByCodeAsc()
                    .map { it.toDto() }
                    .forEach { emit(it) }
            } catch (e: Exception) {
                logger.error("Error fetching coins: ${e.message}", e)
                throw e
            }
        }
    }

    suspend fun getHogaInfo(coinCode: String): HogaDto = withContext(Dispatchers.IO) {
        val cacheKey = "hoga:$coinCode"

        try {
            redisTemplate.opsForValue().get(cacheKey)?.let {
                objectMapper.readValue(it.toString(), HogaDto::class.java)
            } ?: marketDataClient.getHogaInfo(coinCode).also { hoga ->
                redisTemplate.opsForValue().set(
                    cacheKey,
                    objectMapper.writeValueAsString(hoga),
                    Duration.ofSeconds(1)
                )
            }
        } catch (e: Exception) {
            logger.error("Error fetching hoga info for $coinCode: ${e.message}", e)
            throw e
        }
    }

    @Transactional(readOnly = true)
    suspend fun getCoinByCode(code: String): CoinDto = withContext(Dispatchers.IO) {
        try {
            // Redis 캐시 확인
            val cacheKey = "coin:$code"
            redisTemplate.opsForValue().get(cacheKey)?.let {
                objectMapper.readValue(it.toString(), CoinDto::class.java)
            } ?: run {
                // DB에서 조회
                val coin = coinRepository.findByCode(code)
                    ?: throw GlobalException(MessageCode.NOT_FOUND_RESOURCE)

                // DTO 변환
                coin.toDto().also { dto ->
                    // Redis에 캐싱
                    redisTemplate.opsForValue().set(
                        cacheKey,
                        objectMapper.writeValueAsString(dto),
                        Duration.ofMinutes(5)
                    )
                }
            }
        } catch (e: Exception) {
            logger.error("Error fetching coin by code $code: ${e.message}", e)
            throw when (e) {
                is GlobalException -> e
                else -> GlobalException(MessageCode.NOT_FOUND_RESOURCE)
            }
        }
    }

}