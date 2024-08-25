package com.study.mstudy.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfig {

    @Value("${item.insert.thread.name-prefix}")
    private String namePrefix;
    @Value("${item.insert.thread.core-pool-size}")
    private int corePoolSize;
    @Value("${item.insert.thread.max-pool-size}")
    private int maxPoolSize;
    @Value("${item.insert.thread.queue-capacity}")
    private int queueCapacity;

    /**
     * 준배치발송 스레드 풀 생성
     * @return	스레드풀 실행 클래스객체
     */
    @Bean
    public Executor itemThreadExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix(namePrefix);
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.initialize();
        return executor;
    }

}