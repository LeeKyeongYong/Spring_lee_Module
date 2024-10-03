package com.krstudy.kapi.com.krstudy.kapi.global.cheduler

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import jakarta.annotation.PostConstruct
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class JobPostingScheduler(
//    private val resolver: JobPostingResolver,
//    private val jobPostingService: JobPostingService,
//    private val emailSendingService: EmailSendingService
) {

    private val log = LoggerFactory.getLogger(JobPostingScheduler::class.java)

    /**
     * 실행하면 메세지를 보낸다. 수동으로 크롤링을 수행하기 위해 필요함
     */
    @PostConstruct
    fun init() {
        executeRegularUpdate()
    }

    @Async
    @Scheduled(cron = "0 30 18 * * ?")
    fun executeRegularUpdate() {
        log.info("{}일자 스케쥴러를 실행합니다.", LocalDate.now())
        updateServer()
//        val postings = jobPostingService.getNewlyJobPosting()
//        emailSendingService.sendJobPostings(postings)
    }

    private fun updateServer() {
//        val crawlingResults = resolver.crawling()
//        jobPostingService.updateAll(crawlingResults)
    }
}