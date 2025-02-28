/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class BillingDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String owner;
    private String billingType;

    public BillingDetails() {}

    public BillingDetails(BillingDetails value) {
        this.id = value.id;
        this.owner = value.owner;
        this.billingType = value.billingType;
    }

    public BillingDetails(
        Long id,
        String owner,
        String billingType
    ) {
        this.id = id;
        this.owner = owner;
        this.billingType = billingType;
    }

    /**
     * Getter for <code>msa.billing_details.id</code>.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Setter for <code>msa.billing_details.id</code>.
     */
    public BillingDetails setId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Getter for <code>msa.billing_details.owner</code>.
     */
    public String getOwner() {
        return this.owner;
    }

    /**
     * Setter for <code>msa.billing_details.owner</code>.
     */
    public BillingDetails setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    /**
     * Getter for <code>msa.billing_details.billing_type</code>.
     */
    public String getBillingType() {
        return this.billingType;
    }

    /**
     * Setter for <code>msa.billing_details.billing_type</code>.
     */
    public BillingDetails setBillingType(String billingType) {
        this.billingType = billingType;
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
        final BillingDetails other = (BillingDetails) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        }
        else if (!this.id.equals(other.id))
            return false;
        if (this.owner == null) {
            if (other.owner != null)
                return false;
        }
        else if (!this.owner.equals(other.owner))
            return false;
        if (this.billingType == null) {
            if (other.billingType != null)
                return false;
        }
        else if (!this.billingType.equals(other.billingType))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.owner == null) ? 0 : this.owner.hashCode());
        result = prime * result + ((this.billingType == null) ? 0 : this.billingType.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("BillingDetails (");

        sb.append(id);
        sb.append(", ").append(owner);
        sb.append(", ").append(billingType);

        sb.append(")");
        return sb.toString();
    }
}
