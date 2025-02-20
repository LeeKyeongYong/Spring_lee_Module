package com.concurrencycontrol.coupon.annotation;

import java.util.Arrays;
import java.util.stream.IntStream;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 분산 락을 적용하는 Aspect 클래스입니다.
 * @DistributedLock 어노테이션이 적용된 메서드에 대해 락을 적용합니다.
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AspectDistributedLock {

    private final DistributedLockManager lockManager;
    private final RedissonCallTransaction redissonCallTransaction;

    /**
     * @DistributedLock 어노테이션이 적용된 메서드를 감싸서 락을 적용하는 메서드입니다.
     * @param joinPoint 메서드 실행 전후의 정보를 담고 있는 객체
     * @return 메서드 실행 결과
     */
    @Around("@annotation(com.concurrencycontrol.coupon.annotation.DistributedLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) {
        // 메서드 시그니처를 통해 어노테이션 정보를 가져옵니다.
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        DistributedLock distributedLock = signature.getMethod().getAnnotation(DistributedLock.class);

        // 메서드 파라미터 이름과 값을 사용해 락을 위한 키를 생성합니다.
        String key = this.createKey(signature.getParameterNames(), joinPoint.getArgs(), distributedLock.key().split(","));
        log.info("[CREATE REDISSON KEY] KEY : {}", key);

        // DistributedLockManager를 통해 락을 적용하고, 락을 획득한 후 메서드를 실행합니다.
        return lockManager.executeWithLock(distributedLock, key, () -> redissonCallTransaction.proceed(joinPoint));
    }

    /**
     * 메서드 파라미터 이름과 값을 사용하여 락을 위한 키를 생성하는 메서드입니다.
     * @param parameterNames 메서드 파라미터 이름
     * @param args 메서드 파라미터 값
     * @param keys 락 키를 생성하기 위한 파라미터 이름
     * @return 생성된 락 키
     */
    private String createKey(String[] parameterNames, Object[] args, String[] keys) {
        StringBuilder resultKey = new StringBuilder();

        // 키에 해당하는 파라미터만 필터링하여 문자열로 만듭니다.
        IntStream.range(0, parameterNames.length)
                .filter(i -> Arrays.asList(keys).contains(parameterNames[i]))
                .forEach(i -> resultKey.append(parameterNames[i])
                        .append("-")
                        .append(args[i])
                        .append("-"));

        // 마지막 "-"을 제거합니다.
        if (resultKey.length() > 0) {
            resultKey.deleteCharAt(resultKey.length() - 1);
        }

        return resultKey.toString();
    }
}
