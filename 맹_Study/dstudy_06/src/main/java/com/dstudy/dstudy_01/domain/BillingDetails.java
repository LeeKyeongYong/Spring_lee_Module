package com.dstudy.dstudy_01.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BillingDetails {

    @Column(name = "owner")
    private String owner;

    public BillingDetails() {}

    public BillingDetails(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "BillingDetails [owner=" + owner + "]";
    }
}