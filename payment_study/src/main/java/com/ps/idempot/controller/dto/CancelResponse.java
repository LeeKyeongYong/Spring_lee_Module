package com.ps.idempot.controller.dto;

import com.ps.idempot.domain.CancelPayment;

public record CancelResponse(Long memberId,
                             String idempotency,
                             Long orderId) {
    public static CancelResponse from(final CancelPayment cancelPayment){
        return new CancelResponse(
                cancelPayment.getMemberId(),
                cancelPayment.getIdempotency(),
                cancelPayment.getOrderId()
        );
    }
}
