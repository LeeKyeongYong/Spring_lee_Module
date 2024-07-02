package com.ps.idempot.controller;

import com.ps.idempot.controller.dto.CancelResponse;
import com.ps.idempot.controller.dto.PayOrderResponse;
import com.ps.idempot.domain.CancelPayment;
import com.ps.idempot.domain.Payment;
import com.ps.idempot.service.PaymentService;
import com.ps.idempot.service.dto.PayOrderRequest;
import com.ps.idempot.service.dto.PaymentCancelRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/payments")
    public ResponseEntity<PayOrderResponse> payOrder(
            @RequestHeader(name = "Idempotency-Key") final String idempotency,
            @RequestBody final PayOrderRequest payOrderRequest
    ) {
        Payment payment = paymentService.pay(idempotency, payOrderRequest);
        return ResponseEntity.ok(PayOrderResponse.from(payment));
    }

    @PostMapping("/payments/cancel")
    public ResponseEntity<CancelResponse> cancelOrder(
            @RequestHeader(name = "Idempotency-Key") final String idempotency,
            @RequestBody final PaymentCancelRequest paymentCancelRequest
    ) {
        CancelPayment cancel = paymentService.cancel(idempotency, paymentCancelRequest);
        return ResponseEntity.ok(CancelResponse.from(cancel));
    }
}
