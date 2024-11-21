package com.krstudy.kapi.domain.kafkaproducer.rw

import com.krstudy.kapi.domain.kafkaproducer.entity.WebLog
import net.datafaker.Faker
import org.springframework.batch.item.ItemReader
import java.util.UUID
import java.util.concurrent.TimeUnit

class WebLogItemReader : ItemReader<WebLog> {

    private val faker = Faker()
    private val genCount = 100
    private var currentCount = 0

    override fun read(): WebLog? {
        return if (currentCount <= genCount) {
            currentCount++
            genNewWebLog()
        } else {
            null
        }
    }

    private fun genNewWebLog(): WebLog {
        val webLog = WebLog()
        webLog.apply {
            ipAddress = faker.internet().ipV4Address()
            url = faker.internet().url()
            userId = faker.idNumber().singaporeanFinBefore2000()
            sessionId = UUID.randomUUID().toString()
            timestamp = faker.date().past(2, TimeUnit.DAYS).toString()
        }
        return webLog
    }
}