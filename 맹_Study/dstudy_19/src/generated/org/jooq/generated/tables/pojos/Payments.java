/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables.pojos;


import java.io.Serializable;
import java.time.LocalDateTime;

import org.jooq.generated.enums.PaymentsStatus;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Payments implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer amount;
    private Integer remainingAmount;
    private LocalDateTime completedAt;
    private LocalDateTime createDate;
    private LocalDateTime createdAt;
    private Long id;
    private Long memberId;
    private LocalDateTime modifyDate;
    private Long version;
    private String orderId;
    private String paymentKey;
    private PaymentsStatus status;

    public Payments() {}

    public Payments(Payments value) {
        this.amount = value.amount;
        this.remainingAmount = value.remainingAmount;
        this.completedAt = value.completedAt;
        this.createDate = value.createDate;
        this.createdAt = value.createdAt;
        this.id = value.id;
        this.memberId = value.memberId;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.orderId = value.orderId;
        this.paymentKey = value.paymentKey;
        this.status = value.status;
    }

    public Payments(
        Integer amount,
        Integer remainingAmount,
        LocalDateTime completedAt,
        LocalDateTime createDate,
        LocalDateTime createdAt,
        Long id,
        Long memberId,
        LocalDateTime modifyDate,
        Long version,
        String orderId,
        String paymentKey,
        PaymentsStatus status
    ) {
        this.amount = amount;
        this.remainingAmount = remainingAmount;
        this.completedAt = completedAt;
        this.createDate = createDate;
        this.createdAt = createdAt;
        this.id = id;
        this.memberId = memberId;
        this.modifyDate = modifyDate;
        this.version = version;
        this.orderId = orderId;
        this.paymentKey = paymentKey;
        this.status = status;
    }

    /**
     * Getter for <code>msa.payments.amount</code>.
     */
    public Integer getAmount() {
        return this.amount;
    }

    /**
     * Setter for <code>msa.payments.amount</code>.
     */
    public Payments setAmount(Integer amount) {
        this.amount = amount;
        return this;
    }

    /**
     * Getter for <code>msa.payments.remaining_amount</code>.
     */
    public Integer getRemainingAmount() {
        return this.remainingAmount;
    }

    /**
     * Setter for <code>msa.payments.remaining_amount</code>.
     */
    public Payments setRemainingAmount(Integer remainingAmount) {
        this.remainingAmount = remainingAmount;
        return this;
    }

    /**
     * Getter for <code>msa.payments.completed_at</code>.
     */
    public LocalDateTime getCompletedAt() {
        return this.completedAt;
    }

    /**
     * Setter for <code>msa.payments.completed_at</code>.
     */
    public Payments setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
        return this;
    }

    /**
     * Getter for <code>msa.payments.create_date</code>.
     */
    public LocalDateTime getCreateDate() {
        return this.createDate;
    }

    /**
     * Setter for <code>msa.payments.create_date</code>.
     */
    public Payments setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    /**
     * Getter for <code>msa.payments.created_at</code>.
     */
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Setter for <code>msa.payments.created_at</code>.
     */
    public Payments setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Getter for <code>msa.payments.id</code>.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Setter for <code>msa.payments.id</code>.
     */
    public Payments setId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Getter for <code>msa.payments.member_id</code>.
     */
    public Long getMemberId() {
        return this.memberId;
    }

    /**
     * Setter for <code>msa.payments.member_id</code>.
     */
    public Payments setMemberId(Long memberId) {
        this.memberId = memberId;
        return this;
    }

    /**
     * Getter for <code>msa.payments.modify_date</code>.
     */
    public LocalDateTime getModifyDate() {
        return this.modifyDate;
    }

    /**
     * Setter for <code>msa.payments.modify_date</code>.
     */
    public Payments setModifyDate(LocalDateTime modifyDate) {
        this.modifyDate = modifyDate;
        return this;
    }

    /**
     * Getter for <code>msa.payments.version</code>.
     */
    public Long getVersion() {
        return this.version;
    }

    /**
     * Setter for <code>msa.payments.version</code>.
     */
    public Payments setVersion(Long version) {
        this.version = version;
        return this;
    }

    /**
     * Getter for <code>msa.payments.order_id</code>.
     */
    public String getOrderId() {
        return this.orderId;
    }

    /**
     * Setter for <code>msa.payments.order_id</code>.
     */
    public Payments setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    /**
     * Getter for <code>msa.payments.payment_key</code>.
     */
    public String getPaymentKey() {
        return this.paymentKey;
    }

    /**
     * Setter for <code>msa.payments.payment_key</code>.
     */
    public Payments setPaymentKey(String paymentKey) {
        this.paymentKey = paymentKey;
        return this;
    }

    /**
     * Getter for <code>msa.payments.status</code>.
     */
    public PaymentsStatus getStatus() {
        return this.status;
    }

    /**
     * Setter for <code>msa.payments.status</code>.
     */
    public Payments setStatus(PaymentsStatus status) {
        this.status = status;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Payments other = (Payments) obj;
        if (this.amount == null) {
            if (other.amount != null)
                return false;
        }
        else if (!this.amount.equals(other.amount))
            return false;
        if (this.remainingAmount == null) {
            if (other.remainingAmount != null)
                return false;
        }
        else if (!this.remainingAmount.equals(other.remainingAmount))
            return false;
        if (this.completedAt == null) {
            if (other.completedAt != null)
                return false;
        }
        else if (!this.completedAt.equals(other.completedAt))
            return false;
        if (this.createDate == null) {
            if (other.createDate != null)
                return false;
        }
        else if (!this.createDate.equals(other.createDate))
            return false;
        if (this.createdAt == null) {
            if (other.createdAt != null)
                return false;
        }
        else if (!this.createdAt.equals(other.createdAt))
            return false;
        if (this.id == null) {
            if (other.id != null)
                return false;
        }
        else if (!this.id.equals(other.id))
            return false;
        if (this.memberId == null) {
            if (other.memberId != null)
                return false;
        }
        else if (!this.memberId.equals(other.memberId))
            return false;
        if (this.modifyDate == null) {
            if (other.modifyDate != null)
                return false;
        }
        else if (!this.modifyDate.equals(other.modifyDate))
            return false;
        if (this.version == null) {
            if (other.version != null)
                return false;
        }
        else if (!this.version.equals(other.version))
            return false;
        if (this.orderId == null) {
            if (other.orderId != null)
                return false;
        }
        else if (!this.orderId.equals(other.orderId))
            return false;
        if (this.paymentKey == null) {
            if (other.paymentKey != null)
                return false;
        }
        else if (!this.paymentKey.equals(other.paymentKey))
            return false;
        if (this.status == null) {
            if (other.status != null)
                return false;
        }
        else if (!this.status.equals(other.status))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.amount == null) ? 0 : this.amount.hashCode());
        result = prime * result + ((this.remainingAmount == null) ? 0 : this.remainingAmount.hashCode());
        result = prime * result + ((this.completedAt == null) ? 0 : this.completedAt.hashCode());
        result = prime * result + ((this.createDate == null) ? 0 : this.createDate.hashCode());
        result = prime * result + ((this.createdAt == null) ? 0 : this.createdAt.hashCode());
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.memberId == null) ? 0 : this.memberId.hashCode());
        result = prime * result + ((this.modifyDate == null) ? 0 : this.modifyDate.hashCode());
        result = prime * result + ((this.version == null) ? 0 : this.version.hashCode());
        result = prime * result + ((this.orderId == null) ? 0 : this.orderId.hashCode());
        result = prime * result + ((this.paymentKey == null) ? 0 : this.paymentKey.hashCode());
        result = prime * result + ((this.status == null) ? 0 : this.status.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Payments (");

        sb.append(amount);
        sb.append(", ").append(remainingAmount);
        sb.append(", ").append(completedAt);
        sb.append(", ").append(createDate);
        sb.append(", ").append(createdAt);
        sb.append(", ").append(id);
        sb.append(", ").append(memberId);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(orderId);
        sb.append(", ").append(paymentKey);
        sb.append(", ").append(status);

        sb.append(")");
        return sb.toString();
    }
}
