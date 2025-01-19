package com.dstudy.dstudy_01.domain;

import jakarta.persistence.*;


@Entity
@Table(name = "bank_account")
public class BankAccount extends BillingDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "account")
    private String account;

    @Column(name = "bankname")
    private String bankname;

    @Column(name = "swift")
    private String swift;

    public BankAccount() {
        super();
    }

    public BankAccount(String owner, String account, String bankname, String swift) {
        super(owner);
        this.account = account;
        this.bankname = bankname;
        this.swift = swift;
    }

    public Long getId() {
        return id;
    }

}