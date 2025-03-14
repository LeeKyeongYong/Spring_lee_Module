package com.study.aop003.domain;

import java.util.Date;

public class Customer {
    private int no;
    private String name;
    private String email;
    private String phone;
    private Date birthDate;

    public Customer() {
    }

    public Customer(int no, String name, String email, String phone, Date birthDate) {
        this.no = no;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return "Customer [no=" + no + ", name=" + name + ", email=" + email
                + ", phone=" + phone + ", birthDate=" + birthDate + "]";
    }
}