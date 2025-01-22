package com.dstudy.dstudy_01.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "credit_card")
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("CC")
public class CreditCard extends BillingDetails {

    @Column(name = "number")
    private String number;

    @Column(name = "exp_month")
    private String expMonth;

    @Column(name = "exp_year")
    private String expYear;

    public CreditCard() {}

    public CreditCard(String number, String expMonth, String expYear) {
        this.number = number;
        this.expMonth = expMonth;
        this.expYear = expYear;
    }

    // Getters and Setters remain the same
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getExpMonth() {
        return expMonth;
    }

    public void setExpMonth(String expMonth) {
        this.expMonth = expMonth;
    }

    public String getExpYear() {
        return expYear;
    }

    public void setExpYear(String expYear) {
        this.expYear = expYear;
    }

    @Override
    public String toString() {
        return "CreditCard [number=" + number +
                ", expMonth=" + expMonth +
                ", expYear=" + expYear +
                ", getOwner()=" + getOwner() + "]";
    }
}