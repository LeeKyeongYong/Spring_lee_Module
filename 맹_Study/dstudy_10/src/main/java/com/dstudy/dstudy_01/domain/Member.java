package com.dstudy.dstudy_01.domain;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "birth_date", nullable = true)
    private LocalDate birthDate = LocalDate.now();

    @ElementCollection
    @CollectionTable(
            name = "member_phone",
            joinColumns = @JoinColumn(name = "member_id")
    )
    @Column(name = "phone")
    private Set<String> phone = new HashSet<>();

    public Member() {}

    public Member(Long id, String name, String email, Set<String> phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public Member(Long id, String name, String email, Set<String> phone, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Set<String> getPhone() {
        return phone;
    }

    public void setPhone(Set<String> phone) {
        this.phone = phone;
    }
    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}