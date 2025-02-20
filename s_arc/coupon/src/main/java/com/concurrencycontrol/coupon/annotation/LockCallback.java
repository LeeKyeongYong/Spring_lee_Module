package com.concurrencycontrol.coupon.annotation;

/**
 * 락을 사용하여 작업을 실행하는 콜백 인터페이스입니다.
 * 이 인터페이스는 락을 획득한 후 실행할 작업을 정의하는 역할을 합니다.
 * @FunctionalInterface로 선언하여 람다식으로 사용할 수 있습니다.
 */
@FunctionalInterface
public interface LockCallback {
    /**
     * 락을 획득한 후 실행할 작업을 정의하는 메서드입니다.
     * @return 작업의 실행 결과
     * @throws Throwable 작업 실행 중 발생할 수 있는 예외
     */
    Object executeLocked() throws Throwable;
}
