package com.springstudy.concurrency.racecondition.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock을 사용한 고급 카운터 서비스
 * <p>
 * ReentrantLock은 synchronized보다 더 많은 기능을 제공:
 * - 공정성 설정 가능 (fairness)
 * - 타임아웃 지원
 * - 인터럽트에 응답 가능
 * - 대기 상태 확인 가능
 * - 조건 변수(Condition) 지원
 * </p>
 */
@Slf4j
@Service
public class ReentrantLockCounterService {

    private static final Logger log = LoggerFactory.getLogger(ReentrantLockCounterService.class);

    private int count = 0;
    private int maxCount = Integer.MAX_VALUE;
    private final ReentrantLock lock = new ReentrantLock(true); // 공정성 보장 락
    private final Condition thresholdReached = lock.newCondition();

    /**
     * ReentrantLock을 사용한 스레드 안전 증가 연산
     * @return 증가 후 카운터 값
     * @throws InterruptedException 대기 중 인터럽트 발생 시
     */
    public int increment() throws InterruptedException {
        lock.lockInterruptibly();  // 인터럽트에 응답 가능한 락 획득
        try {
            // 최대값 도달 시 대기
            while (count >= maxCount) {
                log.warn("최대 카운터 값 {}에 도달했습니다. 대기합니다.", maxCount);
                thresholdReached.await(100, TimeUnit.MILLISECONDS);
            }

            count++;

            // 값이 변경되었으므로 조건 신호 발생
            thresholdReached.signalAll();

            return count;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 타임아웃 기능이 있는 증가 연산
     * @param timeout 대기 시간
     * @param unit 시간 단위
     * @return 성공 여부
     */
    public boolean incrementWithTimeout(long timeout, TimeUnit unit) {
        try {
            if (lock.tryLock(timeout, unit)) {
                try {
                    count++;
                    return true;
                } finally {
                    lock.unlock();
                }
            }
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("인터럽트 발생으로 증가 연산 중단");
            return false;
        }
    }

    /**
     * 현재 카운터 값 반환 (읽기 전용 연산은 짧은 락 사용)
     */
    public int getCount() {
        lock.lock();
        try {
            return count;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 최대 카운터 값 설정
     * @param maxCount 새 최대값
     */
    public void setMaxCount(int maxCount) {
        lock.lock();
        try {
            this.maxCount = maxCount;
            // 새 최대값이 현재 값보다 크면 대기 중인 스레드에게 신호
            if (maxCount > count) {
                thresholdReached.signalAll();
            }
            log.info("최대 카운터 값이 {}로 설정되었습니다", maxCount);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 카운터 리셋
     */
    public void reset() {
        lock.lock();
        try {
            count = 0;
            thresholdReached.signalAll();
            log.info("카운터가 리셋되었습니다");
        } finally {
            lock.unlock();
        }
    }

    /**
     * 락 경합 상태 정보 반환
     */
    public LockStatistics getLockStatistics() {
        return new LockStatistics(
                lock.isLocked(),
                lock.isHeldByCurrentThread(),
                lock.getQueueLength(),
                lock.hasQueuedThreads()
        );
    }

    /**
     * 락 상태 정보를 담는 레코드
     */
    public record LockStatistics(
            boolean locked,
            boolean heldByCurrentThread,
            int queueLength,
            boolean hasQueuedThreads
    ) {}
}