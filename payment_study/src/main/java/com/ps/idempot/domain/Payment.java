package com.ps.idempot.domain;

import com.ps.idempot.domain.service.IdempotencyValidator;
import com.ps.idempot.domain.vo.Status;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.ApplicationEventPublisher;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private String idempotency;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    public static Payment createStatusOfProgress(
            final String idempotency,
            final Long memberId,
            final Long orderId,
            final IdempotencyValidator idempotencyValidator
    ) {
        idempotencyValidator.isValidRule(idempotency);
        return Payment.builder()
                .memberId(memberId)
                .idempotency(idempotency)
                .orderId(orderId)
                .status(Status.PROGRESS)
                .build();
    }

}
