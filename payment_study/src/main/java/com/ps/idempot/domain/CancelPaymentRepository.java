package com.ps.idempot.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CancelPaymentRepository extends JpaRepository<CancelPayment,Long> {

    boolean existsByIdempotency(final String idempotency);
    Optional<CancelPayment> findByIdempotency(final String idempotency);

}
