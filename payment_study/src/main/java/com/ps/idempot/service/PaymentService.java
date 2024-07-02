package com.ps.idempot.service;


import com.ps.idempot.domain.CancelPayment;
import com.ps.idempot.domain.CancelPaymentRepository;
import com.ps.idempot.domain.Payment;
import com.ps.idempot.domain.PaymentRepository;
import com.ps.idempot.domain.event.CanceledPaymentEvent;
import com.ps.idempot.domain.event.CreatedPaymentEvent;
import com.ps.idempot.domain.service.IdempotencyValidator;
import com.ps.idempot.domain.vo.PayMoney;
import com.ps.idempot.event.Events;
import com.ps.idempot.service.dto.PayOrderRequest;
import com.ps.idempot.service.dto.PaymentCancelRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final CancelPaymentRepository cancelPaymentRepository;
    private final IdempotencyValidator idempotencyValidator;
    private final com.ps.idempot.service.CardValidationRequester cardValidationRequester;

    @Transactional
    public Payment pay(final String idempotency, final PayOrderRequest request) {
        // 이미 존재하는 결제요청이라면 그대로 반환 or 409 Conflict
        if (paymentRepository.existsByIdempotency(idempotency)) {
            return paymentRepository.findByIdempotency(idempotency).get();
        }

        // 카드 유효성 검사
        validateCard(request);

        // 결제요청 생성 및 결제 요청 전송 (실패 한다면 타임아웃)
        Payment payment = paymentRepository.save(createNewPayment(idempotency, request));

        // 이벤트를 통해 결제 처리와 함께 통계자료로 처리
        Events.raise(new CreatedPaymentEvent(idempotency, request.orderId()));
        return payment;
    }

    @Transactional
    public CancelPayment cancel(final String idempotency, final PaymentCancelRequest paymentCancelRequest) {
        // 이미 존재하는 취소 요청이라면 그대로 반환 or 409 Conflict
        if (cancelPaymentRepository.existsByIdempotency(idempotency)) {
            return cancelPaymentRepository.findByIdempotency(idempotency).get();
        }

        // 결제요청 생성 및 결제 요청 전송 (실패 한다면 타임아웃)
        CancelPayment cancelPayment = cancelPaymentRepository.save(createNewCancelPayment(idempotency, paymentCancelRequest));

        // 이벤트를 통해 결제 취소 처리와 함께 통계자료로 처리
        Events.raise(new CanceledPaymentEvent(idempotency, paymentCancelRequest.orderId()));
        return cancelPayment;
    }

    // 결제 요청 객체 생성
    private Payment createNewPayment(final String idempotency, final PayOrderRequest request) {
        return Payment.createStatusOfProgress(
                idempotency,
                request.memberId(),
                request.orderId(),
                idempotencyValidator
        );
    }

    // 카드 검증
    private void validateCard(final PayOrderRequest request) {
        PayMoney card = PayMoney.createWithValidateDate(
                request.cardNumber(),
                request.cardExpirationYear(),
                request.cardExpirationMonth(),
                request.cardNumber()
        );

        cardValidationRequester.validate(card);
    }

    // 결제 취소 객체 생성
    private CancelPayment createNewCancelPayment(final String idempotency, final PaymentCancelRequest request) {
        return CancelPayment.createStatusOfProgress(
                idempotency,
                request.memberId(),
                request.orderId(),
                idempotencyValidator
        );
    }
}
