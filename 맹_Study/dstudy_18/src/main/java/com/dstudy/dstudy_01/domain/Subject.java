package com.dstudy.dstudy_01.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "subject")
@Getter
@Setter
@ToString(exclude = "lectures")
@EqualsAndHashCode(exclude = "lectures")
@NoArgsConstructor
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "hours")
    private Integer hours;

    @Temporal(TemporalType.DATE)
    @Column(name = "open_date")
    private Date openDate;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Lecture> lectures = new HashSet<>();

    public Subject(Long id, String name, Integer hours, Date openDate) {
        this.id = id;
        this.name = name;
        this.hours = hours;
        this.openDate = openDate;
    }

    public void addLecture(Lecture lecture) {
        this.lectures.add(lecture);
        lecture.setSubject(this);
        lecture.setId(this.id);
    }
}