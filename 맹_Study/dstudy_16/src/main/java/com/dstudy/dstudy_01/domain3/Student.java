package com.dstudy.dstudy_01.domain3;

import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "student")
@IdClass(Student.Hakbun.class)
public class Student {
    public static class Hakbun implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        private Integer hak;
        private Integer ban;
        private Integer bun;

        public Hakbun() {}

        public Hakbun(Integer hak, Integer ban, Integer bun) {
            this.hak = hak;
            this.ban = ban;
            this.bun = bun;
        }

        public Integer getHak() { return hak; }
        public void setHak(Integer hak) { this.hak = hak; }
        public Integer getBan() { return ban; }
        public void setBan(Integer ban) { this.ban = ban; }
        public Integer getBun() { return bun; }
        public void setBun(Integer bun) { this.bun = bun; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Hakbun hakbun = (Hakbun) o;
            return Objects.equals(hak, hakbun.hak) &&
                    Objects.equals(ban, hakbun.ban) &&
                    Objects.equals(bun, hakbun.bun);
        }

        @Override
        public int hashCode() {
            return Objects.hash(hak, ban, bun);
        }
    }

    @Id
    @Column(name = "hak")
    private Integer hak;

    @Id
    @Column(name = "ban")
    private Integer ban;

    @Id
    @Column(name = "bun")
    private Integer bun;

    @Column(name = "name")
    private String name;

    @Column(name = "gender")
    private String gender;

    @Temporal(TemporalType.DATE)
    @Column(name = "birth_date")
    private Date birthDate;

    public Student() {}

    public Integer getHak() { return hak; }
    public void setHak(Integer hak) { this.hak = hak; }
    public Integer getBan() { return ban; }
    public void setBan(Integer ban) { this.ban = ban; }
    public Integer getBun() { return bun; }
    public void setBun(Integer bun) { this.bun = bun; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public Date getBirthDate() { return birthDate; }
    public void setBirthDate(Date birthDate) { this.birthDate = birthDate; }
}