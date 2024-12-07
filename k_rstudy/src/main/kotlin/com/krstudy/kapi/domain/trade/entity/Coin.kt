package com.krstudy.kapi.domain.trade.entity

import com.krstudy.kapi.domain.trade.dto.CoinDto
import com.krstudy.kapi.global.jpa.BaseEntity
import com.krstudy.kapi.global.exception.GlobalException
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "coins")
class Coin(
    @Id
    @Column(length = 20)
    val code: String,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false, precision = 19, scale = 8)
    var currentPrice: BigDecimal,

    @Column(nullable = false, precision = 19, scale = 8)
    var changeRate: BigDecimal,

    @Column(nullable = false, precision = 19, scale = 8)
    var volume24h: BigDecimal,

    @Column(nullable = false)
    var active: Boolean = true  // 활성화 여부 필드 추가
) : BaseEntity() {

    fun toDto(): CoinDto = CoinDto(
        code = code,
        name = name,
        currentPrice = currentPrice,
        changeRate = changeRate,
        volume24h = volume24h
    )

    companion object {
        fun create(
            code: String,
            name: String,
            currentPrice: BigDecimal,
            changeRate: BigDecimal = BigDecimal.ZERO,
            volume24h: BigDecimal = BigDecimal.ZERO
        ): Coin {
            return Coin(
                code = code,
                name = name,
                currentPrice = currentPrice,
                changeRate = changeRate,
                volume24h = volume24h
            )
        }
    }
}