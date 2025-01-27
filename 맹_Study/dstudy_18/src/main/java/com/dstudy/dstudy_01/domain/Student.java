package com.dstudy.dstudy_01.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "student")
@Getter
@Setter
@ToString(exclude = "lectures")
@EqualsAndHashCode(exclude = "lectures")
@NoArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    private Long no;

    @Column(name = "name")
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Lecture> lectures = new HashSet<>();

    public Student(Long no, String name, Date birthDate, String email) {
        this.no = no;
        this.name = name;
        this.birthDate = birthDate;
        this.email = email;
    }

    public void addLecture(Lecture lecture) {
        this.lectures.add(lecture);
        lecture.setStudent(this);
        lecture.setNo(this.no);
    }
}