package com.dstudy.dstudy_01.domain2;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "address")
@Getter
@Setter
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "post_code")
    private String postCode;

    @Column(name = "city")
    private String city;

    @Column(name = "address")
    private String address;

    @OneToOne
    @PrimaryKeyJoinColumn
    private Member member;

    public Address() {}
}