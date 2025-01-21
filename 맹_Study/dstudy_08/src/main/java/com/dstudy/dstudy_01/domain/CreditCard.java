package com.dstudy.dstudy_01.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@DiscriminatorValue("CC")
@Getter @Setter @NoArgsConstructor @ToString(callSuper = true)
public class CreditCard extends BillingDetails {

    @Column(name = "number")
    private String number;

    @Column(name = "exp_month")
    private String expMonth;

    @Column(name = "exp_year")
    private String expYear;

    @Builder
    public CreditCard(String number, String expMonth, String expYear) {
        this.number = number;
        this.expMonth = expMonth;
        this.expYear = expYear;
    }
}