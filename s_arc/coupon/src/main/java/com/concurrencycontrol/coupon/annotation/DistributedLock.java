package com.concurrencycontrol.coupon.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 메서드에 분산 잠금을 적용하는 커스텀 어노테이션입니다.
 * Redis 기반의 분산 잠금을 관리하기 위해 사용됩니다.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    /**
     * Redis 잠금을 위한 고유 키입니다.
     * 이 키는 메서드 파라미터와 관련이 있어야 하며, 잠금을 적절히 관리할 수 있도록 합니다.
     */
    String key();

    /**
     * 잠금 획득 및 리스 시간의 단위입니다.
     * 기본값은 초(SECONDS)입니다.
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 잠금을 획득하기 위한 대기 시간입니다.
     * 이 시간 내에 잠금을 획득하지 못하면 실패로 처리됩니다.
     */
    long waitTime() default 5L;

    /**
     * 잠금을 획득한 후 자동으로 해제되는 시간입니다.
     * 이 시간이 지나면 잠금이 자동으로 해제됩니다.
     */
    long leaseTime() default 3L;

}