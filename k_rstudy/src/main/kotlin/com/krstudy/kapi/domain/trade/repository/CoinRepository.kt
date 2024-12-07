package com.krstudy.kapi.domain.trade.repository

import com.krstudy.kapi.domain.trade.entity.Coin
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface CoinRepository : JpaRepository<Coin, String> {

    fun findAllByOrderByCodeAsc(): List<Coin>

    fun findByCode(code: String): Coin?

    @Query("SELECT c FROM Coin c WHERE c.active = true ORDER BY c.code")
    fun findAllActiveCoin(): List<Coin>
}