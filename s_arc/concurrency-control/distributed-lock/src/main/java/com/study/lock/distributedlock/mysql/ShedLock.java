package com.study.lock.distributedlock.mysql;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
public class ShedLock {
    private final JdbcTemplate jdbcTemplate;
    private static final Duration LOCK_DURATION = Duration.ofMinutes(15);
    private static final Duration MAX_LOCK_DURATION = Duration.ofMinutes(30);

    public boolean executeWithLock(String jobName, Runnable task) {
        String serverIdentifier = ServerIdentifierUtil.getServerIdentifier();
        LocalDateTime now = LocalDateTime.now();

        try {
            if (!acquireLock(jobName, serverIdentifier, now)) {
                log.info("작업 {} 에 대한 락 획득 실패", jobName);
                return false;
            }

            try {
                task.run();
                updateLockStatus(jobName, LockStatus.COMPLETED, null);
                return true;
            } catch (Exception e) {
                String errorMessage = ExceptionUtil.getErrorMessage(e);
                updateLockStatus(jobName, LockStatus.FAILED, errorMessage);
                throw e;
            }
        } catch (Exception e) {
            log.error("작업 실행 실패: {}", jobName, e);
            return false;
        }
    }

    private boolean acquireLock(String jobName, String serverIdentifier, LocalDateTime now) {
        String sql = """
            INSERT INTO shedlock (name, locked_at, locked_by, lock_until, status)
            VALUES (?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
                locked_at = ?,
                locked_by = ?,
                lock_until = ?,
                status = ?
            WHERE TIMESTAMPDIFF(MINUTE, locked_at, ?) >= ?
            """;

        try {
            int updated = jdbcTemplate.update(sql,
                    jobName, now, serverIdentifier, now.plus(MAX_LOCK_DURATION),
                    LockStatus.RUNNING.name(),
                    now, serverIdentifier, now.plus(MAX_LOCK_DURATION),
                    LockStatus.RUNNING.name(),
                    now, MAX_LOCK_DURATION.toMinutes()
            );
            return updated > 0;
        } catch (Exception e) {
            log.error("락 획득 실패", e);
            return false;
        }
    }

    private void updateLockStatus(String jobName, LockStatus status, String errorMessage) {
        String sql = """
            UPDATE shedlock
            SET status = ?,
                completed_at = NOW(),
                error_message = ?
            WHERE name = ?
            """;
        jdbcTemplate.update(sql, status.name(), errorMessage, jobName);
    }

    private enum LockStatus {
        RUNNING, COMPLETED, FAILED
    }
}