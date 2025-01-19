package com.dstudy.dstudy_01.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "credit_card")
public class CreditCard extends BillingDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "number")
    private String number;

    @Column(name = "exp_month")
    private String expMonth;

    @Column(name = "exp_year")
    private String expYear;

    public CreditCard() {
    }

    public CreditCard(String owner, String number, String expMonth, String expYear) {
        super(owner);
        this.number = number;
        this.expMonth = expMonth;
        this.expYear = expYear;
    }

    public Long getId() {
        return id;
    }

}