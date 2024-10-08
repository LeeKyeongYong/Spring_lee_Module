package com.jstudy.hibernate2.domain.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.persistence.*;
import jakarta.persistence.GenerationType;


@Entity
@Table(name = "student")
public class Student implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_sequence")
    @SequenceGenerator(name = "student_sequence", sequenceName = "student_id_seq", allocationSize = 1)
    private Long id;

    private String name;
    private String email;
    private String phone;

    @Temporal(TemporalType.DATE)
    private Date birthdate;

    private Integer age;

    @ManyToMany
    @JoinTable(name = "enroll",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "no"))
    private List<Subject> subjects = new ArrayList<>();

    public Student() {}

    public Student(Long id, String name, String email, String phone,
                   Date birthdate, Integer age) {
        super();
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.birthdate = birthdate;
        this.age = age;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    @Override
    public String toString() {
        return "Student [id=" + id + ", name=" + name + ", email="
                + email + ", phone=" + phone + ", birthdate=" + birthdate
                + ", age=" + age + "]";
    }

}
