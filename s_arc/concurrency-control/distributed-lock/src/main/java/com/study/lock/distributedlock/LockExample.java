package com.study.lock.distributedlock;

import com.study.lock.distributedlock.mysql.ShedLock;
import com.study.lock.distributedlock.redis.RedisPubSubLock;
import com.study.lock.distributedlock.redis.RedisSpinLock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LockExample {
    private final ShedLock shedLock;
    private final RedisPubSubLock redisPubSubLock;
    private final RedisSpinLock redisSpinLock;

    // MySQL ShedLock 사용 예제
    public void executeShedLockExample() {
        shedLock.executeWithLock("주문처리작업", () -> {
            log.info("주문 처리 시작");
            processOrders();
            log.info("주문 처리 완료");
        });
    }

    // Redis PubSub Lock 사용 예제
    public void executeRedisPubSubLockExample() {
        redisPubSubLock.executeWithLock("재고업데이트작업", () -> {
            log.info("재고 업데이트 시작");
            updateInventory();
            log.info("재고 업데이트 완료");
        });
    }

    // Redis Spin Lock 사용 예제
    public void executeRedisSpinLockExample() {
        redisSpinLock.executeWithLock("결제처리작업", () -> {
            log.info("결제 처리 시작");
            processPayments();
            log.info("결제 처리 완료");
        });
    }

    // 누락된 메서드들 구현
    private void processOrders() {
        // 주문 처리 로직 구현
        log.info("주문 처리 로직 실행 중...");
    }

    private void updateInventory() {
        // 재고 업데이트 로직 구현
        log.info("재고 업데이트 로직 실행 중...");
    }

    private void processPayments() {
        // 결제 처리 로직 구현
        log.info("결제 처리 로직 실행 중...");
    }
}