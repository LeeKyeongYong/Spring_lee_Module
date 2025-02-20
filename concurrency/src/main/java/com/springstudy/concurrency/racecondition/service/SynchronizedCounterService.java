package com.springstudy.concurrency.racecondition.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * synchronized 키워드를 활용한 고급 카운터 서비스
 * <p>
 * Virtual Thread 환경에서 synchronized 블록 사용 시 고려사항을 포함하여
 * 동기화 메커니즘의 성능과 특성을 모니터링합니다.
 * </p>
 */
@Slf4j
@Service
public class SynchronizedCounterService {

    private static final Logger log = LoggerFactory.getLogger(SynchronizedCounterService.class);

    private int count = 0;
    private final Object lockObject = new Object(); // 전용 락 객체
    private final ConcurrentHashMap<String, Long> lockTimings = new ConcurrentHashMap<>();
    private final AtomicInteger blockingOperations = new AtomicInteger(0);

    /**
     * synchronized 메서드를 통한 증가 연산 (메서드 레벨 동기화)
     * blocking I/O 포함 - Virtual Thread Pinning 발생 가능
     */
    public synchronized void increment() {
        String operationId = UUID.randomUUID().toString();
        long startTime = System.nanoTime();
        blockingOperations.incrementAndGet();

        try {
            // 블로킹 I/O 연산 - 가상 스레드에서 pinning 발생 가능
            log.debug("Blocking I/O operation started by thread: {}", Thread.currentThread());
            Thread.sleep(1000);
            count++;
            log.debug("Counter incremented to {} by thread: {}", count, Thread.currentThread());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Increment operation interrupted");
        } finally {
            long endTime = System.nanoTime();
            lockTimings.put(operationId, endTime - startTime);
            blockingOperations.decrementAndGet();
        }
    }

    /**
     * synchronized 블록을 통한 증가 연산 (블록 레벨 동기화)
     * No I/O - Virtual Thread가 효율적으로 동작 가능
     */
    public void incrementNonBlocking() {
        String operationId = UUID.randomUUID().toString();
        long startTime = System.nanoTime();

        synchronized (lockObject) {
            // CPU 작업만 수행 - 가상 스레드에서 효율적
            count++;
            log.debug("Counter incremented to {} by thread: {}", count, Thread.currentThread());
        }

        long endTime = System.nanoTime();
        lockTimings.put(operationId, endTime - startTime);
    }

    /**
     * synchronized를 사용한 안전한 getter
     */
    public synchronized int getCount() {
        return count;
    }

    /**
     * 락 동작 통계 조회
     */
    public LockStatistics getLockStatistics() {
        // 락 타이밍 통계 계산
        long avgNanos = lockTimings.isEmpty() ? 0 :
                lockTimings.values().stream()
                        .mapToLong(Long::longValue)
                        .sum() / lockTimings.size();

        return new LockStatistics(
                lockTimings.size(),
                TimeUnit.NANOSECONDS.toMicros(avgNanos),
                blockingOperations.get()
        );
    }

    /**
     * 락 통계 정보 레코드
     */
    public record LockStatistics(
            int totalOperations,
            long avgLockTimeInMicros,
            int currentBlockingOperations
    ) {}

    /**
     * 카운터와 통계 리셋
     */
    public synchronized void reset() {
        count = 0;
        lockTimings.clear();
        log.info("Counter and statistics have been reset");
    }
}