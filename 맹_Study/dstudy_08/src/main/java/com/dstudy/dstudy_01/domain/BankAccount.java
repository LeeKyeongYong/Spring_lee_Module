package com.dstudy.dstudy_01.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@DiscriminatorValue("BA")
@Getter @Setter @NoArgsConstructor @ToString(callSuper = true)
public class BankAccount extends BillingDetails {

    @Column(name = "account")
    private String account;

    @Column(name = "bankname")
    private String bankname;

    @Column(name = "swift")
    private String swift;

    @Builder
    public BankAccount(String account, String bankname, String swift) {
        this.account = account;
        this.bankname = bankname;
        this.swift = swift;
    }
}