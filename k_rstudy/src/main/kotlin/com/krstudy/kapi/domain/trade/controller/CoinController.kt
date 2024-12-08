package com.krstudy.kapi.domain.trade.controller

import com.krstudy.kapi.domain.trade.dto.OrderForm
import com.krstudy.kapi.domain.trade.service.CoinService
import com.krstudy.kapi.global.https.ReqData
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import com.krstudy.kapi.global.exception.GlobalException
import com.krstudy.kapi.global.exception.MessageCode
import org.springframework.data.redis.core.RedisTemplate
import org.slf4j.LoggerFactory
import org.slf4j.Logger

@Controller
class CoinController(
    private val rq: ReqData,
    private val coinService: CoinService,
    private val redisTemplate: RedisTemplate<String, Any>

) {

    @GetMapping("/service")
    suspend fun tradeService(): String {
        val member = rq.getMember() ?: throw GlobalException(MessageCode.UNAUTHORIZED)

        try {
            // 현재 선택된 코인 정보 가져오기
            val coinCode = redisTemplate.opsForValue()
                .get("coin:${member.userid}:code")?.toString() ?: "BTC"

            // 코인 정보 조회
            val coin = coinService.getCoinByCode(coinCode)

            // 호가 정보 조회
            val hogaInfo = coinService.getHogaInfo(coinCode)

            // 주문 폼 객체 생성
            val buyOrder = OrderForm(
                coinName = coin.name,
                price = null,
                quantity = null,
                total = null
            )

            // 모델에 데이터 추가
            rq.setAttribute("member", member)
            rq.setAttribute("coin", coin)
            rq.setAttribute("hogaList", hogaInfo)
            rq.setAttribute("buyOrder", buyOrder)

            return "domain/dashboard/tradeService"

        } catch (e: Exception) {
            logger.error("Error in trade service: ${e.message}", e)
            throw when (e) {
                is GlobalException -> e
                else -> GlobalException(MessageCode.SYSTEM_ERROR)
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(CoinController::class.java)
    }

}