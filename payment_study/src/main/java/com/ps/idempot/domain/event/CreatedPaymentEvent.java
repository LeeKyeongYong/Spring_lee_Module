package com.ps.idempot.domain.event;

public record CreatedPaymentEvent(String idempotency,Long orderId) {
}
