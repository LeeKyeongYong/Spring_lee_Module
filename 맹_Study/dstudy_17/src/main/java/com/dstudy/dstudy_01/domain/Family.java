package com.dstudy.dstudy_01.domain;

import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "family")
@IdClass(Family.Ids.class)
public class Family {
    public static class Ids implements Serializable {

        @Id
        @Column(name = "id")
        private Long id;

        @Id
        @Column(name = "birth_date")
        @Temporal(TemporalType.DATE)
        private Date birthDate;

        @Id
        @Column(name = "name")
        private String name;

        public Ids() {}

        public Ids(Long id, Date birthDate, String name) {
            this.id = id;
            this.birthDate = birthDate;
            this.name = name;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Date getBirthDate() { return birthDate; }
        public void setBirthDate(Date birthDate) { this.birthDate = birthDate; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Ids ids = (Ids) o;
            return Objects.equals(id, ids.id) &&
                    Objects.equals(birthDate, ids.birthDate) &&
                    Objects.equals(name, ids.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, birthDate, name);
        }
    }

    @Id
    @Column(name = "id")
    private Long id;

    @Id
    @Temporal(TemporalType.DATE)
    @Column(name = "birth_date")
    private Date birthDate;

    @Id
    @Column(name = "name")
    private String name;

    @Column(name = "gender")
    private String gender;

    @ManyToOne
    @JoinColumn(name = "employee_id",insertable = false, updatable = false)
    private Employee employee;

    public Family() {}

    public Family(Date birthDate, String name, String gender) {
        this.birthDate = birthDate;
        this.name = name;
        this.gender = gender;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Date getBirthDate() { return birthDate; }
    public void setBirthDate(Date birthDate) { this.birthDate = birthDate; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
}