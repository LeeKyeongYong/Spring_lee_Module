package com.springstudy.concurrency.racecondition.service;

import com.springstudy.concurrency.racecondition.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * 다양한 동시성 제어 기법의 카운터 서비스를 테스트하는 클래스
 * <p>
 * 이 테스트 클래스는 다음 기능을 포함합니다:
 * - JDK 19+ 가상 스레드 지원 확인 및 테스트
 * - 다양한 스레드 풀 구성에서의 성능 비교
 * - 파라미터화된 테스트를 통한 다양한 부하 시나리오 검증
 * - Assertion 기반 테스트 및 벤치마크 측정
 * </p>
 */
@Slf4j
@DisplayName("카운터 서비스 동시성 테스트")
class CounterServiceTest {
    private static final Logger log = LoggerFactory.getLogger(CounterServiceTest.class);
    private CounterService counterService;
    private SynchronizedCounterService synchronizedCounterService;
    private ReentrantLockCounterService reentrantLockCounterService;
    private AtomicCounterService atomicCounterService;
    private SemaphoreCounterService semaphoreCounterService;

    private static final int EXPECTED_COUNT = 10000;
    private static final int DEFAULT_THREAD_COUNT = 10;
    private static final int DEFAULT_ITERATIONS = 1000;

    @BeforeEach
    void setUp() {
        counterService = new CounterService();
        synchronizedCounterService = new SynchronizedCounterService();
        reentrantLockCounterService = new ReentrantLockCounterService();
        atomicCounterService = new AtomicCounterService();
        semaphoreCounterService = new SemaphoreCounterService(1, true);

        log.info("테스트 환경 준비 완료: JDK 버전 {}", System.getProperty("java.version"));
    }

    /**
     * 다양한 ExecutorService로 동시 증가 연산 실행
     *
     * @param incrementFunction 증가 연산 함수
     * @param threadCount 스레드 수
     * @param iterations 반복 횟수
     * @param executorSupplier ExecutorService 공급자
     * @return 실행 시간 (밀리초)
     */
    private long runConcurrentIncrements(
            Runnable incrementFunction,
            int threadCount,
            int iterations,
            Supplier<ExecutorService> executorSupplier) throws InterruptedException {

        long startTime = System.currentTimeMillis();
        int totalIncrements = threadCount * iterations;

        try (ExecutorService executor = executorSupplier.get()) {
            List<Future<?>> futures = new ArrayList<>(totalIncrements);

            // 모든 작업 제출
            for (int i = 0; i < totalIncrements; i++) {
                futures.add(executor.submit(incrementFunction));
            }

            // 모든 작업 완료 대기
            for (Future<?> future : futures) {
                try {
                    future.get();
                } catch (ExecutionException e) {
                    log.error("작업 실행 중 오류 발생", e);
                    fail("작업 실행 실패: " + e.getMessage());
                }
            }
        }

        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    /**
     * 가상 스레드 지원 여부 확인
     */
    private boolean isVirtualThreadSupported() {
        try {
            Thread.ofVirtual().start(() -> {}).join();
            return true;
        } catch (NoSuchMethodError | UnsupportedOperationException e) {
            return false;
        }
    }

    @Test
    @DisplayName("Race Condition이 발생하는 기본 Counter 테스트")
    void testRaceCondition() throws InterruptedException {
        long executionTime = runConcurrentIncrements(
                counterService::increment,
                DEFAULT_THREAD_COUNT,
                DEFAULT_ITERATIONS,
                () -> Executors.newFixedThreadPool(DEFAULT_THREAD_COUNT)
        );

        int actualCount = counterService.getCount();
        log.info("기본 카운터 결과: {} (예상: {}), 실행 시간: {}ms",
                actualCount, EXPECTED_COUNT, executionTime);

        // Race Condition으로 인해 카운트가 예상값보다 적을 것
        assertNotEquals(EXPECTED_COUNT, actualCount,
                "Race Condition으로 인해 예상값과 실제값이 달라야 함");
        assertTrue(actualCount < EXPECTED_COUNT,
                "Race Condition으로 인해 실제 카운트가 예상보다 적어야 함");
    }

    @Test
    @DisplayName("synchronized 키워드를 사용한 동기화 테스트")
    void testSynchronizedCounter() throws InterruptedException {
        long executionTime = runConcurrentIncrements(
                synchronizedCounterService::incrementNonBlocking,
                DEFAULT_THREAD_COUNT,
                DEFAULT_ITERATIONS,
                () -> Executors.newFixedThreadPool(DEFAULT_THREAD_COUNT)
        );

        int actualCount = synchronizedCounterService.getCount();
        log.info("synchronized 카운터 결과: {} (예상: {}), 실행 시간: {}ms",
                actualCount, EXPECTED_COUNT, executionTime);

        assertEquals(EXPECTED_COUNT, actualCount,
                "synchronized로 인해 Race Condition이 방지되어야 함");
    }

    @Test
    @DisplayName("ReentrantLock을 사용한 동기화 테스트")
    void testReentrantLockCounter() throws InterruptedException {
        AtomicInteger failedAttempts = new AtomicInteger(0);

        Runnable incrementTask = () -> {
            try {
                reentrantLockCounterService.increment();
            } catch (InterruptedException e) {
                failedAttempts.incrementAndGet();
                Thread.currentThread().interrupt();
            }
        };

        long executionTime = runConcurrentIncrements(
                incrementTask,
                DEFAULT_THREAD_COUNT,
                DEFAULT_ITERATIONS,
                () -> Executors.newFixedThreadPool(DEFAULT_THREAD_COUNT)
        );

        int actualCount = reentrantLockCounterService.getCount();
        log.info("ReentrantLock 카운터 결과: {} (예상: {}), 실행 시간: {}ms, 실패: {}",
                actualCount, EXPECTED_COUNT, executionTime, failedAttempts.get());

        assertEquals(EXPECTED_COUNT, actualCount,
                "ReentrantLock으로 인해 Race Condition이 방지되어야 함");
        assertEquals(0, failedAttempts.get(), "모든 증가 연산이 성공해야 함");
    }

    @Test
    @DisplayName("AtomicInteger를 사용한 원자적 연산 테스트")
    void testAtomicCounter() throws InterruptedException {
        long executionTime = runConcurrentIncrements(
                atomicCounterService::incrementAtomic,
                DEFAULT_THREAD_COUNT,
                DEFAULT_ITERATIONS,
                () -> Executors.newFixedThreadPool(DEFAULT_THREAD_COUNT)
        );

        int actualCount = atomicCounterService.getCount();
        log.info("Atomic 카운터 결과: {} (예상: {}), 실행 시간: {}ms",
                actualCount, EXPECTED_COUNT, executionTime);

        assertEquals(EXPECTED_COUNT, actualCount,
                "AtomicInteger로 인해 Race Condition이 방지되어야 함");
    }

    @Test
    @DisplayName("Semaphore를 사용한 동기화 테스트")
    void testSemaphoreCounter() throws InterruptedException {
        long executionTime = runConcurrentIncrements(
                semaphoreCounterService::increment,
                DEFAULT_THREAD_COUNT,
                DEFAULT_ITERATIONS,
                () -> Executors.newFixedThreadPool(DEFAULT_THREAD_COUNT)
        );

        int actualCount = semaphoreCounterService.getCount();
        log.info("Semaphore 카운터 결과: {} (예상: {}), 실행 시간: {}ms",
                actualCount, EXPECTED_COUNT, executionTime);

        assertEquals(EXPECTED_COUNT, actualCount,
                "Semaphore로 인해 Race Condition이 방지되어야 함");
    }

    @Test
    @DisplayName("가상 스레드를 사용한 동시성 테스트")
    void testWithVirtualThreads() throws InterruptedException {
        // 가상 스레드 지원 확인
        assumeTrue(isVirtualThreadSupported(), "이 테스트는 JDK 19+ 및 가상 스레드를 지원하는 환경에서만 실행됩니다");

        log.info("가상 스레드 테스트 시작");
        long atomicExecutionTime = runConcurrentIncrements(
                atomicCounterService::incrementAtomic,
                100, // 더 많은 동시 작업
                1000,
                () -> Executors.newVirtualThreadPerTaskExecutor()
        );

        int actualCount = atomicCounterService.getCount();
        log.info("가상 스레드 Atomic 카운터 결과: {} (예상: {}), 실행 시간: {}ms",
                actualCount, 100000, atomicExecutionTime);

        assertEquals(100000, actualCount,
                "가상 스레드를 사용해도 AtomicInteger는 정확하게 동작해야 함");
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 100, 1000})
    @DisplayName("다양한 부하 상황에서의 동시성 테스트")
    void testWithDifferentLoads(int threadCount) throws InterruptedException {
        // 테스트 전 모든 카운터 초기화
        counterService.reset();
        synchronizedCounterService.reset();
        reentrantLockCounterService.reset();
        atomicCounterService.reset();
        semaphoreCounterService.reset();

        int iterations = EXPECTED_COUNT / threadCount;
        int expectedTotal = threadCount * iterations;

        log.info("부하 테스트 시작: 스레드 {}, 반복 {}, 총 연산 {}",
                threadCount, iterations, expectedTotal);

        // 고정 스레드 풀 생성
        ExecutorService threadPool = Executors.newFixedThreadPool(threadCount);

        // 모든 구현에 대해 동일한 작업 부하 적용
        CompletableFuture<Void> atomicFuture = CompletableFuture.runAsync(() -> {
            try {
                runConcurrentIncrements(
                        atomicCounterService::incrementAtomic,
                        threadCount, iterations,
                        () -> threadPool
                );
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        CompletableFuture<Void> synchronizedFuture = CompletableFuture.runAsync(() -> {
            try {
                runConcurrentIncrements(
                        synchronizedCounterService::incrementNonBlocking,
                        threadCount, iterations,
                        () -> threadPool
                );
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        CompletableFuture<Void> reentrantLockFuture = CompletableFuture.runAsync(() -> {
            try {
                runConcurrentIncrements(
                        () -> {
                            try {
                                reentrantLockCounterService.increment();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        },
                        threadCount, iterations,
                        () -> threadPool
                );
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // 모든 작업 완료 대기
        CompletableFuture.allOf(atomicFuture, synchronizedFuture, reentrantLockFuture).join();
        threadPool.shutdown();

        // 결과 확인
        assertEquals(expectedTotal, atomicCounterService.getCount(),
                "Atomic 카운터는 정확한 결과를 보여야 함");
        assertEquals(expectedTotal, synchronizedCounterService.getCount(),
                "Synchronized 카운터는 정확한 결과를 보여야 함");
        assertEquals(expectedTotal, reentrantLockCounterService.getCount(),
                "ReentrantLock 카운터는 정확한 결과를 보여야 함");

        log.info("다양한 부하 테스트 완료: 모든 동기화 메커니즘이 올바르게 동작");
    }

    @Test
    @DisplayName("블로킹 I/O와 synchronized - 가상 스레드 Pinning 테스트")
    void testVirtualThreadPinning() throws InterruptedException {
        assumeTrue(isVirtualThreadSupported(), "이 테스트는 가상 스레드를 지원하는 환경에서만 실행됩니다");

        // 적은 수의 가상 스레드로 블로킹 I/O가 있는 synchronized 메서드 호출
        int threadCount = 10;
        int iterations = 2;

        // 테스트 시작 전 상태 초기화
        synchronizedCounterService.reset();

        long startTime = System.currentTimeMillis();
        runConcurrentIncrements(
                synchronizedCounterService::increment, // 블로킹 I/O가 있는 synchronized 메서드
                threadCount, iterations,
                () -> Executors.newVirtualThreadPerTaskExecutor()
        );
        long executionTime = System.currentTimeMillis() - startTime;

        // 블로킹 I/O를 포함한 synchronized 메서드는 가상 스레드 Pinning을 발생시킴
        // 각 작업이 1초 sleep하므로, 완전 병렬화된다면 약 1초 정도 걸려야 함
        // Pinning이 발생하면 직렬화되어 threadCount * iterations * sleepTime(1초) 정도 소요됨
        log.info("가상 스레드 Pinning 테스트 결과: 실행 시간 {}ms", executionTime);

        // 실행 시간이 (스레드 수 * 반복 횟수 * 0.5초) 보다 크면 Pinning 의심
        boolean pinningDetected = executionTime > (threadCount * iterations * 500);

        if (pinningDetected) {
            log.warn("가상 스레드 Pinning이 감지되었습니다 - 블로킹 I/O와 synchronized 조합이 문제를 일으킵니다");
        } else {
            log.info("가상 스레드 Pinning이 감지되지 않았습니다");
        }

        // Pinning 여부와 관계없이 결과는 정확해야 함
        assertEquals(threadCount * iterations, synchronizedCounterService.getCount(),
                "Pinning 발생 여부와 관계없이 카운트는 정확해야 함");
    }

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    @DisplayName("락 타임아웃 테스트")
    void testLockTimeout() throws InterruptedException {
        // 첫 번째 스레드가 락을 오래 점유
        CompletableFuture<Void> longHoldingTask = CompletableFuture.runAsync(() -> {
            try {
                assertTrue(reentrantLockCounterService.incrementWithTimeout(100, TimeUnit.MILLISECONDS));
                // 락을 오래 보유
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // 다른 스레드들은 짧은 타임아웃으로 접근 시도
        Thread.sleep(500); // 첫 번째 스레드가 락을 획득할 시간

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        List<CompletableFuture<Void>> tasks = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            tasks.add(CompletableFuture.runAsync(() -> {
                boolean success = reentrantLockCounterService.incrementWithTimeout(500, TimeUnit.MILLISECONDS);
                if (success) {
                    successCount.incrementAndGet();
                } else {
                    failureCount.incrementAndGet();
                }
            }));
        }

        // 모든 작업 완료 대기
        CompletableFuture.allOf(
                longHoldingTask,
                CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0]))
        ).join();

        log.info("락 타임아웃 테스트 결과: 성공 {}, 실패 {}", successCount.get(), failureCount.get());
        assertTrue(failureCount.get() > 0, "일부 스레드는 타임아웃으로 실패해야 함");
    }
}