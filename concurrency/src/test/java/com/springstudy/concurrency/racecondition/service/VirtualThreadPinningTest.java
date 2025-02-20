package com.springstudy.concurrency.racecondition.service;

import com.springstudy.concurrency.racecondition.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.JRE;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 가상 스레드(Virtual Thread)의 Pinning 현상 테스트
 * <p>
 * JDK 19+ 환경에서 가상 스레드 사용 시 발생할 수 있는 Carrier Thread Pinning 현상을
 * 다양한 동기화 메커니즘과 I/O 작업 상황에서 테스트합니다.
 * </p>
 * <p>
 * Carrier Thread Pinning은 가상 스레드가 synchronized 블록이나 모니터 락을 사용할 때
 * 블로킹 연산을 만나면 캐리어 스레드에 고정되어 언마운트되지 않는 현상을 말합니다.
 * 이는 스레드 풀의 효율성을 저하시킬 수 있습니다.
 * </p>
 */
@Slf4j
@Tag("VirtualThread")
@EnabledOnJre(JRE.JAVA_19)
class VirtualThreadPinningTest {

    private Object lock;
    private ReentrantLock reentrantLock;
    private SynchronizedCounterService synchronizedCounterService;
    private List<String> threadList;
    private AtomicInteger completedTasks;

    @BeforeEach
    void setUp() {
        lock = new Object();
        reentrantLock = new ReentrantLock();
        synchronizedCounterService = new SynchronizedCounterService();
        threadList = new CopyOnWriteArrayList<>();
        completedTasks = new AtomicInteger(0);

        String javaVersion = System.getProperty("java.version");
        log.info("테스트 시작: Java 버전 {}", javaVersion);
    }

    /**
     * 지정된 수의 가상 스레드로 태스크 실행
     *
     * @param task 실행할 작업
     * @param taskCount 실행할 작업 수
     * @param timeout 타임아웃(초)
     * @return 평균 실행 시간 (밀리초)
     */
    private double runConcurrentTasksWithMetrics(Runnable task, int taskCount, int timeout) {
        long startTime = System.currentTimeMillis();
        completedTasks.set(0);

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<?>> futures = new ArrayList<>();

            for (int i = 0; i < taskCount; i++) {
                futures.add(executor.submit(() -> {
                    task.run();
                    completedTasks.incrementAndGet();
                }));
            }

            executor.shutdown();
            boolean completed = executor.awaitTermination(timeout, TimeUnit.SECONDS);

            if (!completed) {
                log.warn("작업이 타임아웃 내에 완료되지 않았습니다. 완료된 작업: {}/{}",
                        completedTasks.get(), taskCount);
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("작업 실행 중 인터럽트 발생", e);
        }

        long duration = System.currentTimeMillis() - startTime;
        log.info("실행 완료: {}개 중 {}개 작업 완료, 총 소요시간: {}ms",
                taskCount, completedTasks.get(), duration);

        // 평균 작업 시간 계산 (완료된 작업만)
        return completedTasks.get() > 0 ? (double) duration / completedTasks.get() : 0;
    }

    /**
     * 피보나치 수열 계산 (CPU 부하 작업)
     */
    private static long fibonacci(int n) {
        if (n <= 1) return n;
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    /**
     * 고유한 스레드 수 카운트
     */
    private int countUniqueThreads() {
        return (int) threadList.stream().distinct().count();
    }

    @Test
    @DisplayName("Virtual Thread에서 synchronized + I/O 사용 시 Pinning 테스트")
    void testSynchronizedPinningWithIo() {
        int taskCount = 100;

        Runnable synchronizedTask = () -> {
            synchronized (lock) {
                String threadName = Thread.currentThread().toString();
                threadList.add(threadName);

                try {
                    // 블로킹 I/O 연산
                    Thread.sleep(100);
                    log.debug("스레드 {} 작업 완료", threadName);
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                    log.error("작업 중단", e);
                }
            }
        };

        double avgExecutionTime = runConcurrentTasksWithMetrics(synchronizedTask, taskCount, 30);
        int uniqueThreads = countUniqueThreads();

        log.info("synchronized + I/O 테스트 결과:");
        log.info("  - 평균 실행 시간: {}ms", String.format("%.2f", avgExecutionTime));
        log.info("  - 사용된 고유 스레드 수: {}", uniqueThreads);
        log.info("  - 완료된 작업 수: {}/{}", completedTasks.get(), taskCount);

        // Pinning이 발생하면 병렬 처리가 저하되어 평균 실행 시간이 증가함
        // 또한 사용된 고유 스레드 수가 적어짐 (캐리어 스레드 수에 제한됨)
        boolean pinningDetected = avgExecutionTime > 150 && uniqueThreads < taskCount / 2;

        log.info("Pinning 감지 여부: {}", pinningDetected ? "예" : "아니오");
    }

    @Test
    @DisplayName("Virtual Thread에서 ReentrantLock + I/O 사용 시 Pinning 테스트")
    void testReentrantLockPinningWithIo() {
        int taskCount = 100;

        Runnable reentrantLockTask = () -> {
            reentrantLock.lock();
            try {
                String threadName = Thread.currentThread().toString();
                threadList.add(threadName);

                // 블로킹 I/O 연산
                Thread.sleep(100);
                log.debug("스레드 {} 작업 완료", threadName);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("작업 중단", e);
            } finally {
                reentrantLock.unlock();
            }
        };

        double avgExecutionTime = runConcurrentTasksWithMetrics(reentrantLockTask, taskCount, 30);
        int uniqueThreads = countUniqueThreads();

        log.info("ReentrantLock + I/O 테스트 결과:");
        log.info("  - 평균 실행 시간: {}ms", String.format("%.2f", avgExecutionTime));
        log.info("  - 사용된 고유 스레드 수: {}", uniqueThreads);
        log.info("  - 완료된 작업 수: {}/{}", completedTasks.get(), taskCount);

        // ReentrantLock은 블로킹 I/O 작업에서도 Pinning이 발생하지 않아야 함
        // 이론적으로는 작업이 더 병렬적으로 처리되어야 함
        boolean pinningDetected = avgExecutionTime > 150 && uniqueThreads < taskCount / 2;

        log.info("Pinning 감지 여부: {}", pinningDetected ? "예" : "아니오");
    }

    @Test
    @DisplayName("Virtual Thread에서 synchronized + CPU 부하 사용 시 Pinning 테스트")
    void testSynchronizedPinning() {
        int taskCount = 10;

        Runnable synchronizedTask = () -> {
            synchronized (lock) {
                String threadName = Thread.currentThread().toString();
                threadList.add(threadName);
                log.debug("{} - 락 획득", threadName);

                // CPU 부하 작업
                long result = 0;
                for (int i = 0; i < 1_000_000; i++) {
                    result += Math.sqrt(i);
                }

                log.debug("{} - 락 해제, 결과: {}", threadName, result);
            }
        };

        double avgExecutionTime = runConcurrentTasksWithMetrics(synchronizedTask, taskCount, 60);
        int uniqueThreads = countUniqueThreads();

        log.info("synchronized + CPU 부하 테스트 결과:");
        log.info("  - 평균 실행 시간: {}ms", String.format("%.2f", avgExecutionTime));
        log.info("  - 사용된 고유 스레드 수: {}", uniqueThreads);
        log.info("  - 완료된 작업 수: {}/{}", completedTasks.get(), taskCount);

        // CPU 부하만 있는 경우 명확한 Pinning은 발생하지 않지만,
        // 작업 자체가 CPU 바운드라 병렬성이 제한될 수 있음
        log.info("CPU 바운드 작업은 물리적 코어 수에 제한됨");
    }

    @Test
    @DisplayName("Virtual Thread에서 ReentrantLock + CPU 부하 사용 시 Pinning 테스트")
    void testReentrantLockPinning() {
        int taskCount = 10;

        Runnable reentrantLockTask = () -> {
            reentrantLock.lock();
            try {
                String threadName = Thread.currentThread().toString();
                threadList.add(threadName);
                log.debug("{} - 락 획득", threadName);

                // CPU 부하 작업 (피보나치)
                long result = fibonacci(40);

                log.debug("{} - 락 해제, 결과: {}", threadName, result);
            } finally {
                reentrantLock.unlock();
            }
        };

        double avgExecutionTime = runConcurrentTasksWithMetrics(reentrantLockTask, taskCount, 60);
        int uniqueThreads = countUniqueThreads();

        log.info("ReentrantLock + CPU 부하 테스트 결과:");
        log.info("  - 평균 실행 시간: {}ms", String.format("%.2f", avgExecutionTime));
        log.info("  - 사용된 고유 스레드 수: {}", uniqueThreads);
        log.info("  - 완료된 작업 수: {}/{}", completedTasks.get(), taskCount);
    }

    @Test
    @DisplayName("Virtual Thread에서 락 없이 I/O 작업 실행 (기준 테스트)")
    void testLockFreeIoExecution() {
        int taskCount = 1000;

        Runnable lockFreeTask = () -> {
            String threadName = Thread.currentThread().toString();
            threadList.add(threadName);

            try {
                // 순수 I/O 작업
                Thread.sleep(100);
                log.debug("스레드 {} I/O 작업 완료", threadName);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        double avgExecutionTime = runConcurrentTasksWithMetrics(lockFreeTask, taskCount, 30);
        int uniqueThreads = countUniqueThreads();

        log.info("락 없는 I/O 작업 테스트 결과 (기준):");
        log.info("  - 평균 실행 시간: {}ms", String.format("%.2f", avgExecutionTime));
        log.info("  - 사용된 고유 스레드 수: {}", uniqueThreads);
        log.info("  - 완료된 작업 수: {}/{}", completedTasks.get(), taskCount);

        // 가상 스레드의 이점이 가장 잘 드러나는 케이스
        // 낮은 평균 실행 시간과 높은 고유 스레드 수 예상
    }

    @Test
    @DisplayName("Virtual Thread에서 락 없이 CPU 작업 실행 (기준 테스트)")
    void testLockFreeCpuExecution() {
        int taskCount = 10;

        Runnable lockFreeTask = () -> {
            String threadName = Thread.currentThread().toString();
            threadList.add(threadName);

            // 순수 CPU 작업
            long result = 0;
            for (int i = 0; i < 10_000_000; i++) {
                result += i % 10;
            }

            log.debug("스레드 {} CPU 작업 완료, 결과: {}", threadName, result);
        };

        double avgExecutionTime = runConcurrentTasksWithMetrics(lockFreeTask, taskCount, 30);
        int uniqueThreads = countUniqueThreads();

        log.info("락 없는 CPU 작업 테스트 결과 (기준):");
        log.info("  - 평균 실행 시간: {}ms", String.format("%.2f", avgExecutionTime));
        log.info("  - 사용된 고유 스레드 수: {}", uniqueThreads);
        log.info("  - 완료된 작업 수: {}/{}", completedTasks.get(), taskCount);

        // CPU 바운드 작업은 물리적 코어 수에 의해 제한됨
    }
}