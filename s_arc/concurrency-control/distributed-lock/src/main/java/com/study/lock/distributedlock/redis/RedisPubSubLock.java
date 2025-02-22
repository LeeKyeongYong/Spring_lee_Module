package com.study.lock.distributedlock.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.SetArgs;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.pubsub.RedisPubSubAdapter;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.sync.RedisPubSubCommands;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisPubSubLock implements AutoCloseable {

    private final RedisClient redisClient;
    private final StatefulRedisPubSubConnection<String, String> pubSubConnection;

    private static final String LOCK_PREFIX = "pubsub_lock:";
    private static final String CHANNEL_PREFIX = "lock_channel:";
    private static final Duration LOCK_TIMEOUT = Duration.ofMinutes(30);
    private static final Duration RETRY_INTERVAL = Duration.ofSeconds(1);
    private static final Duration MAX_WAIT_TIME = Duration.ofSeconds(10);

    private final ConcurrentHashMap<String, CountDownLatch> waiters = new ConcurrentHashMap<>();
    private final ScheduledExecutorService renewalScheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> renewalTask;

    @PostConstruct
    public void init() {
        setupPubSubListener();
        enableKeyspaceEvents();
    }

    private void setupPubSubListener() {
        RedisPubSubCommands<String, String> pubSubCommands = pubSubConnection.sync();
        pubSubConnection.addListener(new RedisPubSubAdapter<>() {
            @Override
            public void message(String channel, String message) {
                String lockKey = channel.substring(CHANNEL_PREFIX.length());
                CountDownLatch latch = waiters.get(lockKey);
                if (latch != null) {
                    latch.countDown();
                }
            }
        });

        pubSubCommands.subscribe(CHANNEL_PREFIX + "*");
    }

    private void enableKeyspaceEvents() {
        try (StatefulRedisConnection<String, String> conn = redisClient.connect()) {
            RedisCommands<String, String> commands = conn.sync();
            commands.configSet("notify-keyspace-events", "Ex");
        }
    }

    public boolean executeWithLock(String jobName, Runnable task) {
        String lockKey = LOCK_PREFIX + jobName;
        String lockId = UUID.randomUUID().toString();
        String channel = CHANNEL_PREFIX + jobName;

        try (StatefulRedisConnection<String, String> conn = redisClient.connect()) {
            RedisCommands<String, String> commands = conn.sync();

            if (!tryAcquireLock(commands, lockKey, lockId, channel)) {
                return false;
            }

            try {
                startLockRenewal(commands, lockKey, lockId);
                task.run();
                return true;
            } finally {
                stopLockRenewal();
                releaseLock(commands, lockKey, lockId, channel);
            }
        }
    }

    private boolean tryAcquireLock(RedisCommands<String, String> commands, String lockKey, String lockId, String channel) {
        long deadline = System.currentTimeMillis() + MAX_WAIT_TIME.toMillis();

        while (System.currentTimeMillis() < deadline) {
            if ("OK".equals(commands.set(lockKey, lockId, SetArgs.Builder.nx().px(LOCK_TIMEOUT.toMillis())))) {
                return true;
            }
            waitForLockRelease(lockKey);
        }

        return false;
    }

    private void waitForLockRelease(String lockKey) {
        try {
            CountDownLatch latch = new CountDownLatch(1);
            waiters.put(lockKey, latch);
            latch.await(RETRY_INTERVAL.toMillis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            waiters.remove(lockKey);
        }
    }

    private void startLockRenewal(RedisCommands<String, String> commands, String lockKey, String lockId) {
        renewalTask = renewalScheduler.scheduleAtFixedRate(() -> {
            try {
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
                log.error("Lock renewal failed", e);
            }
        }, LOCK_TIMEOUT.toMillis() / 3, LOCK_TIMEOUT.toMillis() / 3, TimeUnit.MILLISECONDS);
    }

    private void stopLockRenewal() {
        if (renewalTask != null) {
            renewalTask.cancel(false);
        }
    }

    private void releaseLock(RedisCommands<String, String> commands, String lockKey, String lockId, String channel) {
        String script = """
           if redis.call('get', KEYS[1]) == ARGV[1] then
               redis.call('del', KEYS[1])
               redis.call('publish', KEYS[2], 'unlocked')
               return 1
           else
               return 0
           end
           """;

        try {
            commands.eval(script,
                    ScriptOutputType.INTEGER,
                    new String[]{lockKey, channel},
                    lockId
            );
        } catch (Exception e) {
            log.error("Failed to release lock", e);
        }
    }

    @Override
    public void close() {
        renewalScheduler.shutdownNow();
        pubSubConnection.close();
    }
}