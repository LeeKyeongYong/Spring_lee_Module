package com.study.lock.distributedlock.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.SetArgs;
import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisSpinLock implements AutoCloseable {

    private final RedisClient redisClient;

    // 락 관련 상수 정의
    private static final String LOCK_PREFIX = "spinlock:";
    private static final Duration LOCK_TIMEOUT = Duration.ofMinutes(30);
    private static final Duration SPIN_INTERVAL = Duration.ofMillis(100);
    private static final Duration MAX_WAIT_TIME = Duration.ofSeconds(10);

    // 락 갱신을 위한 스케줄러
    private final ScheduledExecutorService renewalScheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> renewalTask;

    public boolean executeWithLock(String jobName, Runnable task) {
        String lockKey = LOCK_PREFIX + jobName;
        String lockId = UUID.randomUUID().toString();

        try (StatefulRedisConnection<String, String> conn = redisClient.connect()) {
            RedisCommands<String, String> commands = conn.sync();

            // 락 획득 시도
            if (!tryAcquireLock(commands, lockKey, lockId)) {
                return false;
            }

            try {
                // 락 획득 성공 시 주기적 갱신 시작
                startLockRenewal(commands, lockKey, lockId);
                task.run();
                return true;
            } finally {
                // 작업 완료 후 정리
                stopLockRenewal();
                releaseLock(commands, lockKey, lockId);
            }
        }
    }

    // 스핀 락 획득 시도 메서드
    private boolean tryAcquireLock(RedisCommands<String, String> commands, String lockKey, String lockId) {
        long deadline = System.currentTimeMillis() + MAX_WAIT_TIME.toMillis();

        while (System.currentTimeMillis() < deadline) {
            // Redis의 SET NX 명령어로 락 획득 시도
            if ("OK".equals(commands.set(lockKey, lockId, SetArgs.Builder.nx().px(LOCK_TIMEOUT.toMillis())))) {
                return true;
            }

            try {
                // 실패 시 일정 시간 대기 후 재시도
                Thread.sleep(SPIN_INTERVAL.toMillis());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return false;
    }

    // 락 갱신 작업 시작
    private void startLockRenewal(RedisCommands<String, String> commands, String lockKey, String lockId) {
        renewalTask = renewalScheduler.scheduleAtFixedRate(() -> {
            try {
                // Lua 스크립트를 사용한 원자적 락 갱신
                String script = """
                   if redis.call('get', KEYS[1]) == ARGV[1] then
                       return redis.call('pexpire', KEYS[1], ARGV[2])
                   else
                       return 0
                   end
                   """;

                commands.eval(script,
                        ScriptOutputType.INTEGER,
                        new String[]{lockKey},
                        lockId,
                        String.valueOf(LOCK_TIMEOUT.toMillis())
                );
            } catch (Exception e) {
                log.error("락 갱신 실패", e);
            }
        }, LOCK_TIMEOUT.toMillis() / 3, LOCK_TIMEOUT.toMillis() / 3, TimeUnit.MILLISECONDS);
    }

    // 락 갱신 작업 중지
    private void stopLockRenewal() {
        if (renewalTask != null) {
            renewalTask.cancel(false);
        }
    }

    // 락 해제
    private void releaseLock(RedisCommands<String, String> commands, String lockKey, String lockId) {
        String script = """
           if redis.call('get', KEYS[1]) == ARGV[1] then
               return redis.call('del', KEYS[1])
           else
               return 0
           end
           """;

        try {
            commands.eval(script,
                    ScriptOutputType.INTEGER,
                    new String[]{lockKey},
                    lockId
            );
        } catch (Exception e) {
            log.error("락 해제 실패", e);
        }
    }

    @Override
    public void close() {
        renewalScheduler.shutdownNow();
    }
}