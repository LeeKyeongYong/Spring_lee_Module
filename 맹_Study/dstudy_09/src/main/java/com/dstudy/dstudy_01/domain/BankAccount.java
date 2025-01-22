package com.dstudy.dstudy_01.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "bank_account")
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("BA")
public class BankAccount extends BillingDetails {

    @Column(name = "account")
    private String account;

    @Column(name = "bankname")
    private String bankname;

    @Column(name = "swift")
    private String swift;

    public BankAccount() {}

    public BankAccount(String account, String bankname, String swift) {
        this.account = account;
        this.bankname = bankname;
        this.swift = swift;
    }

    // Getters and Setters remain the same
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getSwift() {
        return swift;
    }

    public void setSwift(String swift) {
        this.swift = swift;
    }

    @Override
    public String toString() {
        return "BankAccount [account=" + account +
                ", bankname=" + bankname +
                ", swift=" + swift +
                ", getOwner()=" + getOwner() + "]";
    }
}