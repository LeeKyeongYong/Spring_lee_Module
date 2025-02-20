package com.springstudy.concurrency.racecondition.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@DisplayName("카운터 서비스 동시성 테스트")
class CounterServiceTest {
    private static final Logger log = LoggerFactory.getLogger(CounterServiceTest.class);  // SLF4J 로거 사용
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

    private boolean isVirtualThreadSupported() {
        try {
            Thread virtualThread = Thread.ofVirtual().start(() -> {
                try {
                    // 작업 수행
                    Thread.sleep(1000); // 예시로 잠시 대기
                } catch (InterruptedException e) {
                    // 인터럽트 처리
                    Thread.currentThread().interrupt();
                    // 인터럽트가 발생하면 작업을 중단하거나 적절한 처리를 할 수 있음
                }
            });

            // 가상 스레드가 정상적으로 종료될 때까지 기다림
            virtualThread.join();
            return true;
        } catch (NoSuchMethodError | UnsupportedOperationException e) {
            return false;
        } catch (InterruptedException e) {
            // 가상 스레드 시작 중 인터럽트 발생 시 처리
            Thread.currentThread().interrupt();
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
        assumeTrue(isVirtualThreadSupported(), "이 테스트는 JDK 19+ 및 가상 스레드를 지원하는 환경에서만 실행됩니다");

        log.info("가상 스레드 테스트 시작");
        long atomicExecutionTime = runConcurrentIncrements(
                atomicCounterService::incrementAtomic,
                100,
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
        counterService.reset();
        synchronizedCounterService.reset();
        reentrantLockCounterService.reset();
        atomicCounterService.reset();
        semaphoreCounterService.reset();

        int iterations = EXPECTED_COUNT / threadCount;
        int expectedTotal = threadCount * iterations;

        log.info("부하 테스트 시작: 스레드 {}, 반복 {}, 총 연산 {}",
                threadCount, iterations, expectedTotal);

        ExecutorService threadPool = Executors.newFixedThreadPool(threadCount);

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

        CompletableFuture.allOf(atomicFuture, synchronizedFuture, reentrantLockFuture).join();
        threadPool.shutdown();

        assertEquals(expectedTotal, atomicCounterService.getCount(),
                "Atomic 카운터는 정확한 결과를 보여야 함");
        assertEquals(expectedTotal, synchronizedCounterService.getCount(),
                "Synchronized 카운터는 정확한 결과를 보여야 함");
        assertEquals(expectedTotal, reentrantLockCounterService.getCount(),
                "ReentrantLock 카운터는 정확한 결과를 보여야 함");

        log.info("다양한 부하 테스트 완료: 모든 동기화 메커니즘이 올바르게 동작");
    }

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    @DisplayName("락 타임아웃 테스트")
    void testLockTimeout() throws InterruptedException {
        CompletableFuture<Void> longHoldingTask = CompletableFuture.runAsync(() -> {
            try {
                assertTrue(reentrantLockCounterService.incrementWithTimeout(100, TimeUnit.MILLISECONDS));
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread.sleep(500);

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

        CompletableFuture.allOf(
                longHoldingTask,
                CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0]))
        ).join();

        log.info("락 타임아웃 테스트 결과: 성공 {}, 실패 {}", successCount.get(), failureCount.get());
        assertTrue(failureCount.get() > 0, "일부 스레드는 타임아웃으로 실패해야 함");
    }
}
