package com.krstudy.kapi

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.core.task.TaskExecutor
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
@SpringBootApplication
@EnableJpaAuditing
@EnableBatchProcessing
class Main {

    @Bean
    @Primary
    fun taskExecutor(): TaskExecutor {
        val taskExecutor = ThreadPoolTaskExecutor()
        taskExecutor.corePoolSize = 10
        taskExecutor.maxPoolSize = 20
        taskExecutor.setThreadNamePrefix("batch-thread-")
        taskExecutor.initialize()
        return taskExecutor
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(Main::class.java, *args)
   /*
   val applicationContext = SpringApplication.run(Main::class.java, *args)
    System.exit(SpringApplication.exit(applicationContext))
    */
}
//class Main
//
//fun main(args: Array<String>) {
//    runApplication<Main>(*args)
//}
//
