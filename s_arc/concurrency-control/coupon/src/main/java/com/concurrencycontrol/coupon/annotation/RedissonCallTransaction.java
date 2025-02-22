package com.concurrencycontrol.coupon.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Redisson 잠금에 대한 트랜잭션 관리를 처리하는 컴포넌트입니다.
 */
@Component
public class RedissonCallTransaction {

    /**
     * 새로운 트랜잭션에서 메서드를 실행하도록 합니다.
     * 기존 트랜잭션과 독립적인 새로운 트랜잭션에서 실행됩니다.
     * @param joinPoint AOP 실행 지점
     * @return 메서드 실행 결과
     * @throws Throwable 메서드 실행 중 발생할 수 있는 예외
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Object proceed(final ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }
}
