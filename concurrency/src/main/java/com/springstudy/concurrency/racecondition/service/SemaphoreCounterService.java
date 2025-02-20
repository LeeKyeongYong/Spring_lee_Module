package com.springstudy.concurrency.racecondition.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Semaphore를 활용한 고급 카운터 서비스
 * <p>
 * 세마포어는 동시 접근 허용 개수를 제한하는 동기화 기법으로,
 * 다양한 동시성 제어 패턴을 구현할 수 있습니다.
 * </p>
 */
@Slf4j
@Service
public class SemaphoreCounterService {

    private static final Logger log = LoggerFactory.getLogger(SemaphoreCounterService.class);

    private final AtomicInteger count = new AtomicInteger(0);
    private final Semaphore semaphore;
    private final AtomicInteger activeThreads = new AtomicInteger(0);
    private final AtomicInteger totalAttempts = new AtomicInteger(0);
    private final AtomicInteger permitsRefused = new AtomicInteger(0);

    /**
     * 기본 생성자 - 단일 허가 세마포어 (뮤텍스와 동일)
     */
    public SemaphoreCounterService() {
        this(1, true);
    }

    /**
     * 커스텀 세마포어 생성자
     * @param permits 동시 접근 허용 개수
     * @param fair 공정성 여부
     */
    public SemaphoreCounterService(int permits, boolean fair) {
        this.semaphore = new Semaphore(permits, fair);
        log.info("세마포어 카운터 초기화: permits={}, fair={}", permits, fair);
    }

    /**
     * 세마포어를 통한 동기화된 증가 연산
     */
    public void increment() {
        totalAttempts.incrementAndGet();
        try {
            semaphore.acquire();
            try {
                activeThreads.incrementAndGet();
                count.incrementAndGet();
                log.debug("스레드 {}가 카운터를 증가시켰습니다: {}",
                        Thread.currentThread().getName(), count.get());
            } finally {
                activeThreads.decrementAndGet();
                semaphore.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("인터럽트 발생으로 증가 연산 취소");
        }
    }

    /**
     * 타임아웃 기능이 있는 증가 연산
     * @param timeout 대기 시간
     * @param unit 시간 단위
     * @return 성공 여부
     */
    public boolean incrementWithTimeout(long timeout, TimeUnit unit) {
        totalAttempts.incrementAndGet();
        try {
            if (semaphore.tryAcquire(timeout, unit)) {
                try {
                    activeThreads.incrementAndGet();
                    count.incrementAndGet();
                    return true;
                } finally {
                    activeThreads.decrementAndGet();
                    semaphore.release();
                }
            } else {
                permitsRefused.incrementAndGet();
                log.debug("타임아웃으로 증가 연산 거부");
                return false;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("인터럽트 발생으로 증가 연산 취소");
            return false;
        }
    }

    /**
     * 현재 카운터 값 반환
     */
    public int getCount() {
        return count.get();
    }

    /**
     * 카운터 초기화
     */
    public void reset() {
        // 세마포어 리셋은 복잡할 수 있으므로 기존 값만 리셋
        count.set(0);
        totalAttempts.set(0);
        permitsRefused.set(0);
        log.info("카운터가 리셋되었습니다");
    }

    /**
     * 세마포어 통계 정보 반환
     */
    public SemaphoreStatistics getStatistics() {
        return new SemaphoreStatistics(
                semaphore.availablePermits(),
                semaphore.getQueueLength(),
                activeThreads.get(),
                totalAttempts.get(),
                permitsRefused.get()
        );
    }

    /**
     * 세마포어 상태 정보를 담는 레코드
     */
    public record SemaphoreStatistics(
            int availablePermits,
            int queueLength,
            int activeThreads,
            int totalAttempts,
            int permitsRefused
    ) {}
}