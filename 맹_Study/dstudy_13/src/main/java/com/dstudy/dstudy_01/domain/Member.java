package com.dstudy.dstudy_01.domain;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

@Entity
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @ElementCollection
    @CollectionTable(
            name = "member_phone",
            joinColumns = @JoinColumn(name = "member_id")
    )
    private Set<Phone> phones = new HashSet<>();

    public Member() {}

    public Member(String name, String email, Set<Phone> phones) {
        this.name = name;
        this.email = email;
        this.phones = phones != null ? new HashSet<>(phones) : new HashSet<>();
    }

    // Getters and setters with null safety
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

    public Set<Phone> getPhones() {
        return new HashSet<>(phones);
    }

    public void setPhones(Set<Phone> phones) {
        this.phones = phones != null ? new HashSet<>(phones) : new HashSet<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id) &&
                Objects.equals(name, member.name) &&
                Objects.equals(email, member.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email);
    }
}