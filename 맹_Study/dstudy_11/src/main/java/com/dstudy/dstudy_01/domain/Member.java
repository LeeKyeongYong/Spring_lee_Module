package com.dstudy.dstudy_01.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.IndexColumn;
import org.hibernate.annotations.ListIndexJdbcTypeCode;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    @ElementCollection
    @JoinTable(
            name = "member_phone",
            joinColumns = @JoinColumn(name = "id")
    )
    @IndexColumn(name="position",base=1)
    @Column(name = "phone")
    //@OrderBy("position asc")
    private List<String> phone = new ArrayList<>();

    // Constructors
    public Member() {}

    public Member(Long id, String name, String email, List<String> phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<String> getPhone() { return phone; }
    public void setPhone(List<String> phone) { this.phone = phone; }
}