package com.ps.idempot.domain.event;

public record CanceledPaymentEvent(String idempotency, Long orderId) {
}
