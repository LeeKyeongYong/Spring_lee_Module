package com.jstudy.hibernate2.domain.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "subject")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "my_sequence")
    @SequenceGenerator(name = "my_sequence", sequenceName = "subject_no_seq")
    @Column(name = "no")
    private Long no;

    @Column(name = "name")
    private String name;

    @Column(name = "unit")
    private Integer unit;

    @Temporal(TemporalType.DATE)
    @Column(name = "opendate")
    private Date opendate;

    @ManyToMany
    @JoinTable(name = "enroll",
            joinColumns = @JoinColumn(name = "no"),
            inverseJoinColumns = @JoinColumn(name = "id"))
    private List<Student> students = new ArrayList<Student>();

    public Subject() {}

    public Subject(Long no, String name, Integer unit, Date opendate) {
        super();
        this.no = no;
        this.name = name;
        this.unit = unit;
        this.opendate = opendate;
    }

    public Long getNo() {
        return no;
    }

    public void setNo(Long no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUnit() {
        return unit;
    }

    public void setUnit(Integer unit) {
        this.unit = unit;
    }

    public Date getOpendate() {
        return opendate;
    }

    public void setOpendate(Date opendate) {
        this.opendate = opendate;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        return "Subject [no=" + no + ", name=" + name + ", unit=" + unit
                + ", opendate=" + opendate + "]";
    }

}
