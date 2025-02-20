package com.springstudy.concurrency.racecondition.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

/**
 * 원자적 연산을 통한 스레드 안전 카운터 서비스
 * <p>
 * 두 가지 구현 방식을 제공:
 * 1. AtomicInteger: 단일 카운터 값에 대한 원자적 업데이트
 * 2. LongAdder: 고성능 동시성 환경에서 최적화된 카운터 (내부적으로 셀 분할)
 * </p>
 */
@Slf4j
@Service
public class AtomicCounterService {

    private static final Logger log = LoggerFactory.getLogger(AtomicCounterService.class);
    private final AtomicInteger atomicCount = new AtomicInteger(0);
    private final LongAdder longAdderCount = new LongAdder();

    /**
     * AtomicInteger를 사용한 증가 연산
     * @return 증가 후 값
     */
    public int incrementAtomic() {
        int newValue = atomicCount.incrementAndGet();
        log.debug("Incremented atomic counter to: {}", newValue);
        return newValue;
    }

    /**
     * LongAdder를 사용한 증가 연산 (높은 동시성 환경에 최적화)
     */
    public void incrementAdder() {
        longAdderCount.increment();
        log.debug("Incremented long adder counter");
    }

    /**
     * 두 개의 카운터 값을 모두 포함한 카운터 상태 반환
     * @return 현재 카운터 상태
     */
    public CounterState getCounterState() {
        return new CounterState(
                atomicCount.get(),
                longAdderCount.sum()
        );
    }

    /**
     * 각 카운터의 현재 값 반환 (기존 호환성 유지)
     */
    public int getCount() {
        return atomicCount.get();
    }

    /**
     * 모든 카운터 값을 초기화
     */
    public void reset() {
        atomicCount.set(0);
        longAdderCount.reset();
        log.info("All counters have been reset");
    }

    /**
     * 카운터 상태를 저장하는 불변 클래스
     */
    public record CounterState(int atomicValue, long adderValue) {
        @Override
        public String toString() {
            return "CounterState{atomicValue=" + atomicValue +
                    ", adderValue=" + adderValue + "}";
        }
    }
}
