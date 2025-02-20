package com.concurrencycontrol.coupon.annotation;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 분산 락을 관리하고 락을 획득한 후 주어진 작업을 실행하는 클래스입니다.
 * Redisson을 사용하여 분산 락을 처리합니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DistributedLockManager {

    private final RedissonClient redissonClient;

    /**
     * 분산 락을 획득하고, 락을 획득한 후 주어진 콜백을 실행하는 메서드입니다.
     * @param distributedLock 락에 대한 설정을 포함한 어노테이션 객체
     * @param key 락을 걸 키
     * @param callback 락을 획득한 후 실행할 작업을 정의한 콜백
     * @return 작업의 실행 결과
     */
    public Object executeWithLock(DistributedLock distributedLock, String key, LockCallback callback) {

        // Redisson 클라이언트를 사용하여 락을 획득합니다.
        RLock rLock = redissonClient.getLock(key);

        try {
            // 락을 획득하려 시도합니다. 성공하면 true, 실패하면 false를 반환합니다.
            boolean isPossible = rLock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit());

            // 락 획득 실패 시 예외를 던집니다.
            if (!isPossible) {
                throw new RuntimeException("락을 획득할 수 없습니다.");
            }

            // 락을 획득한 후 콜백의 작업을 실행합니다.
            return callback.executeLocked();
        } catch (RuntimeException be) {
            // 예외 발생 시 다시 예외를 던집니다.
            throw be;
        } catch (Throwable e) {
            // 예기치 않은 예외 처리
            e.printStackTrace();
            throw new RuntimeException("예기치 않은 오류 발생");
        } finally {
            // 작업이 끝난 후 락을 해제합니다.
            rLock.unlock();
        }
    }
}
