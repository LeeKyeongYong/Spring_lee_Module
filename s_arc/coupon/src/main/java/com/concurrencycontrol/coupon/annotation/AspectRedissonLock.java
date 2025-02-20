package com.concurrencycontrol.coupon.annotation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AspectRedissonLock {

    private final RedissonClient redissonClient;
    private final RedissonCallTransaction redissonCallTransaction;

    /**
     * 분산 잠금 기능을 처리하는 메서드입니다.
     * @param joinPoint AOP 실행 지점
     * @return 메서드 실행 결과
     * @throws Throwable 잠금 획득 실패나 메서드 실행 중 발생한 예외
     */
    @Around("@annotation(com.concurrencycontrol.coupon.annotation.DistributedLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        // AOP 실행 지점에서 메서드 시그니처 및 메서드를 가져옵니다.
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 메서드에서 DistributedLock 어노테이션을 가져옵니다.
        DistributedLock distributeLock = method.getAnnotation(DistributedLock.class);

        // 메서드의 파라미터를 기반으로 Redis 잠금을 위한 고유 키를 생성합니다.
        String key = this.createKey(signature.getParameterNames(), joinPoint.getArgs(), distributeLock.key().split(","));

        // 생성된 키로 Redisson 잠금을 가져옵니다.
        RLock rLock = redissonClient.getLock(key);

        try {
            // 주어진 시간 내에 잠금을 시도합니다.
            boolean isPossible = rLock.tryLock(distributeLock.waitTime(), distributeLock.leaseTime(), distributeLock.timeUnit());

            // 잠금을 획득하지 못한 경우 예외를 발생시킵니다.
            if (!isPossible) {
                throw new RuntimeException("Unable to acquire lock for key: " + key);
            }

            // 잠금 키가 생성되었음을 로그에 기록합니다.
            log.info("[CREATE REDISSON KEY] KEY : {}", key);

            // 잠금이 성공하면 원래 메서드를 실행합니다.
            return redissonCallTransaction.proceed(joinPoint);

        } catch (RuntimeException be) {
            // 런타임 예외는 그대로 전달합니다.
            throw be;
        } catch (Exception e) {
            // 다른 예외가 발생하면 로그를 찍고 예외를 래핑해서 던집니다.
            log.error("Unexpected error during lock operation", e);
            throw new RuntimeException("Unexpected error during lock operation", e);
        } finally {
            // 메서드 실행 후 잠금을 해제합니다.
            rLock.unlock();
        }
    }

    /**
     * 메서드 파라미터를 기반으로 Redisson 잠금에 사용할 고유 키를 생성합니다.
     * @param parameterNames 메서드의 파라미터 이름들
     * @param args 메서드의 파라미터 값들
     * @param keys 잠금 키로 사용할 파라미터들
     * @return 생성된 고유 키
     */
    private String createKey(String[] parameterNames, Object[] args, String[] keys) {
        StringBuilder resultKey = new StringBuilder();

        // 파라미터들을 돌면서 키와 값을 매칭하여 고유 키를 생성합니다.
        for (int i = 0; i < parameterNames.length; i++) {
            for (String key : keys) {
                if (parameterNames[i].equals(key)) {
                    resultKey.append(key)
                            .append("-")
                            .append(args[i])
                            .append("-"); // 키-값 쌍을 이어서 고유 키를 만듭니다.
                }
            }
        }

        // 마지막 불필요한 하이픈을 제거합니다.
        if (resultKey.length() > 0) {
            resultKey.deleteCharAt(resultKey.length() - 1);
        }

        // 최종 생성된 고유 키를 반환합니다.
        return resultKey.toString();
    }
}