package com.dstudy.dstudy_01.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "billing_details")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "billing_type")
public abstract class BillingDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "owner")
    private String owner;

    public BillingDetails() {}

    public BillingDetails(Long id, String owner) {
        this.id = id;
        this.owner = owner;
    }

    // Getters and Setters remain the same
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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