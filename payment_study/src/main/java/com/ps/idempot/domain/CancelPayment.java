package com.ps.idempot.domain;

import com.ps.idempot.domain.service.IdempotencyValidator;
import com.ps.idempot.domain.vo.Status;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
public class CancelPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String idempotency;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    public static CancelPayment createStatusOfProgress(
            final String idempotency,
            final Long memberId,
            final Long orderId,
            final IdempotencyValidator idempotencyValidator
    ) {
        idempotencyValidator.isValidRule(idempotency);
        return CancelPayment.builder()
                .idempotency(idempotency)
                .memberId(memberId)
                .orderId(orderId)
                .status(Status.PROGRESS)
                .build();
    }

}
