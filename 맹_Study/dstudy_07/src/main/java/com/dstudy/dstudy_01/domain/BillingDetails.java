package com.dstudy.dstudy_01.domain;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "BILLING_TYPE")
public abstract class BillingDetails {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "owner")
    private String owner;

    public BillingDetails() {}

    public BillingDetails(String owner) {
        this.owner = owner;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "BillingDetails [id=" + id + ", owner=" + owner + "]";
    }
}