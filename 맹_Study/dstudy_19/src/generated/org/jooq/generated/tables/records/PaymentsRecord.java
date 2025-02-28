/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables.records;


import java.time.LocalDateTime;

import org.jooq.Record1;
import org.jooq.generated.enums.PaymentsStatus;
import org.jooq.generated.tables.JPayments;
import org.jooq.generated.tables.pojos.Payments;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PaymentsRecord extends UpdatableRecordImpl<PaymentsRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>msa.payments.amount</code>.
     */
    public PaymentsRecord setAmount(Integer value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>msa.payments.amount</code>.
     */
    public Integer getAmount() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>msa.payments.remaining_amount</code>.
     */
    public PaymentsRecord setRemainingAmount(Integer value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>msa.payments.remaining_amount</code>.
     */
    public Integer getRemainingAmount() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>msa.payments.completed_at</code>.
     */
    public PaymentsRecord setCompletedAt(LocalDateTime value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>msa.payments.completed_at</code>.
     */
    public LocalDateTime getCompletedAt() {
        return (LocalDateTime) get(2);
    }

    /**
     * Setter for <code>msa.payments.create_date</code>.
     */
    public PaymentsRecord setCreateDate(LocalDateTime value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>msa.payments.create_date</code>.
     */
    public LocalDateTime getCreateDate() {
        return (LocalDateTime) get(3);
    }

    /**
     * Setter for <code>msa.payments.created_at</code>.
     */
    public PaymentsRecord setCreatedAt(LocalDateTime value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>msa.payments.created_at</code>.
     */
    public LocalDateTime getCreatedAt() {
        return (LocalDateTime) get(4);
    }

    /**
     * Setter for <code>msa.payments.id</code>.
     */
    public PaymentsRecord setId(Long value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>msa.payments.id</code>.
     */
    public Long getId() {
        return (Long) get(5);
    }

    /**
     * Setter for <code>msa.payments.member_id</code>.
     */
    public PaymentsRecord setMemberId(Long value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>msa.payments.member_id</code>.
     */
    public Long getMemberId() {
        return (Long) get(6);
    }

    /**
     * Setter for <code>msa.payments.modify_date</code>.
     */
    public PaymentsRecord setModifyDate(LocalDateTime value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>msa.payments.modify_date</code>.
     */
    public LocalDateTime getModifyDate() {
        return (LocalDateTime) get(7);
    }

    /**
     * Setter for <code>msa.payments.version</code>.
     */
    public PaymentsRecord setVersion(Long value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>msa.payments.version</code>.
     */
    public Long getVersion() {
        return (Long) get(8);
    }

    /**
     * Setter for <code>msa.payments.order_id</code>.
     */
    public PaymentsRecord setOrderId(String value) {
        set(9, value);
        return this;
    }

    /**
     * Getter for <code>msa.payments.order_id</code>.
     */
    public String getOrderId() {
        return (String) get(9);
    }

    /**
     * Setter for <code>msa.payments.payment_key</code>.
     */
    public PaymentsRecord setPaymentKey(String value) {
        set(10, value);
        return this;
    }

    /**
     * Getter for <code>msa.payments.payment_key</code>.
     */
    public String getPaymentKey() {
        return (String) get(10);
    }

    /**
     * Setter for <code>msa.payments.status</code>.
     */
    public PaymentsRecord setStatus(PaymentsStatus value) {
        set(11, value);
        return this;
    }

    /**
     * Getter for <code>msa.payments.status</code>.
     */
    public PaymentsStatus getStatus() {
        return (PaymentsStatus) get(11);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached PaymentsRecord
     */
    public PaymentsRecord() {
        super(JPayments.PAYMENTS);
    }

    /**
     * Create a detached, initialised PaymentsRecord
     */
    public PaymentsRecord(Integer amount, Integer remainingAmount, LocalDateTime completedAt, LocalDateTime createDate, LocalDateTime createdAt, Long id, Long memberId, LocalDateTime modifyDate, Long version, String orderId, String paymentKey, PaymentsStatus status) {
        super(JPayments.PAYMENTS);

        setAmount(amount);
        setRemainingAmount(remainingAmount);
        setCompletedAt(completedAt);
        setCreateDate(createDate);
        setCreatedAt(createdAt);
        setId(id);
        setMemberId(memberId);
        setModifyDate(modifyDate);
        setVersion(version);
        setOrderId(orderId);
        setPaymentKey(paymentKey);
        setStatus(status);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised PaymentsRecord
     */
    public PaymentsRecord(Payments value) {
        super(JPayments.PAYMENTS);

        if (value != null) {
            setAmount(value.getAmount());
            setRemainingAmount(value.getRemainingAmount());
            setCompletedAt(value.getCompletedAt());
            setCreateDate(value.getCreateDate());
            setCreatedAt(value.getCreatedAt());
            setId(value.getId());
            setMemberId(value.getMemberId());
            setModifyDate(value.getModifyDate());
            setVersion(value.getVersion());
            setOrderId(value.getOrderId());
            setPaymentKey(value.getPaymentKey());
            setStatus(value.getStatus());
            resetChangedOnNotNull();
        }
    }
}
