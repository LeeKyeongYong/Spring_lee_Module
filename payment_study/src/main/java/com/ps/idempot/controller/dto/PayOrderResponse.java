package com.ps.idempot.controller.dto;

import  com.ps.idempot.domain.Payment;
public record PayOrderResponse(
        Long memberId,
        String idempotency,
        Long orderId
) {
public static PayOrderResponse from(final Payment payment){
        return new PayOrderResponse(
                payment.getMemberId(),
                payment.getIdempotency(),
                payment.getOrderId()
           );
     }
}
