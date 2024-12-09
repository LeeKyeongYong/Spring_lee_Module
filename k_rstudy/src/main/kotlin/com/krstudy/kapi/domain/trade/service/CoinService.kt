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
    suspend fun getAllCoins(): Flow<CoinDto> = flow {
        try {
            coinRepository.findAllByOrderByCodeAsc()
                .forEach { coin ->
                    emit(coin.toDto())
                }
        } catch (e: Exception) {
            logger.error("Error fetching all coins", e)
            throw GlobalException(MessageCode.NOT_FOUND_RESOURCE)
        }
    }

    suspend fun getHogaInfo(coinCode: String): HogaDto = withContext(Dispatchers.IO) {
        val cacheKey = "hoga:$coinCode"

        try {
            // 캐시에서 먼저 조회
            redisTemplate.opsForValue().get(cacheKey)?.let {
                return@withContext objectMapper.readValue(it.toString(), HogaDto::class.java)
            }

            // 캐시에 없으면 외부 API 호출
            val hogaInfo = marketDataClient.getHogaInfo(coinCode)

            // 캐시에 저장
            redisTemplate.opsForValue().set(
                cacheKey,
                objectMapper.writeValueAsString(hogaInfo),
                Duration.ofSeconds(1)
            )

            hogaInfo
        } catch (e: Exception) {
            logger.error("Error fetching hoga info for coin: $coinCode", e)
            throw GlobalException(MessageCode.NOT_FOUND_RESOURCE)
        }
    }

    @Transactional(readOnly = true)
    suspend fun getCoinByCode(code: String): CoinDto = withContext(Dispatchers.IO) {
        val cacheKey = "coin:$code"

        try {
            // 캐시에서 먼저 조회
            redisTemplate.opsForValue().get(cacheKey)?.let {
                return@withContext objectMapper.readValue(it.toString(), CoinDto::class.java)
            }

            // DB에서 조회
            val coin = coinRepository.findByCode(code)
                ?: throw GlobalException(MessageCode.NOT_FOUND_RESOURCE)

            // DTO 변환 및 캐시 저장
            coin.toDto().also { dto ->
                redisTemplate.opsForValue().set(
                    cacheKey,
                    objectMapper.writeValueAsString(dto),
                    Duration.ofMinutes(5)
                )
            }
        } catch (e: Exception) {
            logger.error("Error fetching coin by code: $code", e)
            when (e) {
                is GlobalException -> throw e
                else -> throw GlobalException(MessageCode.NOT_FOUND_RESOURCE)
            }
        }
    }
}