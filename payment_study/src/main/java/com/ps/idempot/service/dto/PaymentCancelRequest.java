package com.ps.idempot.service.dto;

public record PaymentCancelRequest(
        Long orderId,
        Long memberId
) {
}
