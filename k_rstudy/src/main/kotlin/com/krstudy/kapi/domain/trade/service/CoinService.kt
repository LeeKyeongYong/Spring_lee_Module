package com.krstudy.kapi.domain.trade.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.krstudy.kapi.com.krstudy.kapi.domain.trade.client.MarketDataClient
import com.krstudy.kapi.domain.trade.dto.CoinDto
import com.krstudy.kapi.domain.trade.dto.HogaDto
import com.krstudy.kapi.domain.trade.repository.CoinRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.slf4j.LoggerFactory

@Service
class CoinService(
    private val coinRepository: CoinRepository,
    private val redisTemplate: RedisTemplate<String, Any>,
    private val marketDataClient: MarketDataClient
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional(readOnly = true)
    suspend fun getAllCoins(): Flow<CoinDto> = flow {
        try {
            coinRepository.findAllByOrderByCodeAsc()
                .map { it.toDto() }
                .forEach { emit(it) }
        } catch (e: Exception) {
            logger.error("Error fetching coins: ${e.message}", e)
            throw e
        }
    }

    suspend fun getHogaInfo(coinCode: String): HogaDto {
        val cacheKey = "hoga:$coinCode"

        return try {
            redisTemplate.opsForValue().get(cacheKey)?.let {
                jacksonObjectMapper().readValue(it.toString(), HogaDto::class.java)
            } ?: marketDataClient.getHogaInfo(coinCode)
                .also { hoga ->
                    redisTemplate.opsForValue()
                        .set(cacheKey, jacksonObjectMapper().writeValueAsString(hoga),
                            Duration.ofSeconds(1))
                }
        } catch (e: Exception) {
            logger.error("Error fetching hoga info for $coinCode: ${e.message}", e)
            throw e
        }
    }
}