package com.ps.idempot.domain.service;

public interface IdempotencyValidator {
    void isValidRule(final String idempotency);
}
