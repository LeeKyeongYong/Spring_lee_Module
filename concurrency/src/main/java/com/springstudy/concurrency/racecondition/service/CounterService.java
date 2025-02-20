package com.springstudy.concurrency.racecondition.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

/**
 * 기본 카운터 서비스 - Race Condition 재현용
 * <p>
 * 이 서비스는 의도적으로 스레드 안전하지 않게 설계되어 있으며,
 * 동시성 환경에서의 레이스 컨디션을 시연하는 용도로 사용됩니다.
 * </p>
 */
@Slf4j
@Service
public class CounterService {

    private static final Logger log = LoggerFactory.getLogger(CounterService.class);

    private volatile int count = 0; // volatile 키워드는 가시성은 보장하지만 원자성은 보장하지 않음
    private long totalOperationTimeNanos = 0;
    private int operationCount = 0;

    /**
     * 스레드 안전하지 않은 증가 연산
     * volatile 키워드는 메모리 가시성만 보장하며 원자성은 보장하지 않음
     */
    public void increment() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // 비원자적 연산 - Race Condition 발생 가능
        count++;

        stopWatch.stop();
        recordOperationTime(stopWatch.getTotalTimeNanos());

        if (count % 1000 == 0) {
            log.debug("Counter incremented to: {}", count);
        }
    }

    /**
     * 현재 카운터 값 반환
     */
    public int getCount() {
        return count;
    }

    /**
     * 카운터 초기화
     */
    public void reset() {
        count = 0;
        totalOperationTimeNanos = 0;
        operationCount = 0;
        log.info("Counter has been reset");
    }

    /**
     * 연산 시간 기록 (성능 분석용)
     */
    private synchronized void recordOperationTime(long timeNanos) {
        totalOperationTimeNanos += timeNanos;
        operationCount++;
    }

    /**
     * 평균 연산 시간 반환 (나노초 단위)
     */
    public double getAverageOperationTimeNanos() {
        return operationCount > 0
                ? (double) totalOperationTimeNanos / operationCount
                : 0;
    }
}