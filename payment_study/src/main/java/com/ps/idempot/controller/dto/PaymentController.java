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
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PayOrderResponse> payOrder(
            @RequestHeader(name = "Idempotency-Key") final String idempotency,
            @RequestBody final PayOrderRequest payOrderRequest
    ) {
        validatePayOrderRequest(payOrderRequest);
        Payment payment = paymentService.pay(idempotency, payOrderRequest);
        PayOrderResponse response = PayOrderResponse.from(payment);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cancel")
    public ResponseEntity<CancelResponse> cancelOrder(
            @RequestHeader(name = "Idempotency-Key") final String idempotency,
            @RequestBody final PaymentCancelRequest paymentCancelRequest
    ) {
        validateCancelOrderRequest(paymentCancelRequest);
        CancelPayment cancel = paymentService.cancel(idempotency, paymentCancelRequest);
        CancelResponse response = CancelResponse.from(cancel);
        return ResponseEntity.ok(response);
    }

    // 입력 검증 메서드
    private void validatePayOrderRequest(PayOrderRequest request) {
        if (request == null || request.getAmount() <= 0) {
            throw new IllegalArgumentException("Invalid pay order request");
        }
        // 추가적인 검증 로직을 여기에 추가
    }

    private void validateCancelOrderRequest(PaymentCancelRequest request) {
        if (request == null || request.getOrderId() == null) {
            throw new IllegalArgumentException("Invalid cancel order request");
        }
        // 추가적인 검증 로직을 여기에 추가
    }

    // 예외 처리 메서드
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
    }
}
