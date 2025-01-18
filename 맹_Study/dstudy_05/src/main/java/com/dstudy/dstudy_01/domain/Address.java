package com.dstudy.dstudy_01.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Address {
    @Column(name = "post")
    private String post;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    // 기본 생성자
    public Address() {}

    // 파라미터가 있는 생성자
    public Address(String post, String address, String phone) {
        this.post = post;
        this.address = address;
        this.phone = phone;
    }

    @Override
    public String toString() {
        return new StringBuffer("Address [")
                .append("post=").append(post)
                .append(", address=").append(address)
                .append(", phone=").append(phone)
                .append("]").toString();
    }
}
