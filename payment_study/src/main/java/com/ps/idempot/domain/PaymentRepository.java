package com.ps.idempot.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

        boolean existsByIdempotency(final String idempotency);

        Optional<Payment> findByIdempotency(final String idempotency);
        }
