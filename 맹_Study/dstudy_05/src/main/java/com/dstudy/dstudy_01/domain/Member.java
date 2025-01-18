package com.dstudy.dstudy_01.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    private Long no;

    @Column(name = "name")
    private String name;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "email")
    private String email;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "post", column = @Column(name = "born_post")),
            @AttributeOverride(name = "address", column = @Column(name = "born_address")),
            @AttributeOverride(name = "phone", column = @Column(name = "born_phone"))
    })
    private Address bornAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "post", column = @Column(name = "home_post")),
            @AttributeOverride(name = "address", column = @Column(name = "home_address")),
            @AttributeOverride(name = "phone", column = @Column(name = "home_phone"))
    })
    private Address homeAddress;

    public Member(Long no, String name, LocalDate birthDate, String email,
                  Address bornAddress, Address homeAddress) {
        this.no = no;
        this.name = name;
        this.birthDate = birthDate;
        this.email = email;
        this.bornAddress = bornAddress;
        this.homeAddress = homeAddress;
    }

    public Member() {}

    public String toString() {
        return "Member [no=" + no +
                ", name=" + name +
                ", birthDate=" + birthDate +
                ", email=" + email +
                ", bornAddress=" + bornAddress +
                ", homeAddress=" + homeAddress + "]";
    }
}