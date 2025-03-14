package com.study.aop008.domain;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Customer {
    private int id;
    private String name;
    private String email;
    private String phone;
    private LocalDate birthDate;

    public Customer() {}

    public Customer(int id, String name, String email, String phone, Date birthDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    // 기존 코드와의 호환성을 위한 Date 변환 메서드
    public Date getBirthDateAsDate() {
        return Date.from(this.birthDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    // 기존 코드와의 호환성을 위한 setter
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    @Override
    public String toString() {
        return "Customer [id=" + id + ", name=" + name + ", email=" + email
                + ", phone=" + phone + ", birthDate=" + birthDate + "]";
    }
}