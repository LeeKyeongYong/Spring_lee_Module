package com.sbstudy.basic_lock2.global.standard.dto.retryOnOptimisticLock;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Order(Ordered.LOWEST_PRECEDENCE - 1) // @Transactional보다 더 먼저 메서드에 달라붙어야 해서, 우선순위를 높게 설정
@Component
@Slf4j
public class OptimisticLockingRetryAspect {

    @Around("@annotation(retryOnOptimisticLock)")
    public Object around(ProceedingJoinPoint joinPoint, RetryOnOptimisticLock retryOnOptimisticLock) throws Throwable {
        int maxAttempts = retryOnOptimisticLock.attempts();
        long backoff = retryOnOptimisticLock.backoff();

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                Object o = joinPoint.proceed(); // 메소드 실행

                return o;
            } catch (
                    Exception e
            ) {
                if (attempt < maxAttempts) {
                    Thread.sleep(backoff);
                } else {
                    throw e;
                }
            }
        }

        throw new IllegalStateException("Unexpected state: OptimisticLockingRetryFailed in AOP");
    }
}
