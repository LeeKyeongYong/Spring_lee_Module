package com.dstudy.dstudy_01.domain;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lecture")
@IdClass(Lecture.Ids.class)
@Getter @Setter
@ToString(exclude = {"student", "subject"})
@EqualsAndHashCode(exclude = {"student", "subject"})
@NoArgsConstructor
public class Lecture {
    @Data
    @NoArgsConstructor
    public static class Ids implements Serializable {
        private static final long serialVersionUID = 1L;
        private Long no;
        private Long id;

        public Ids(Long no, Long id) {
            this.no = no;
            this.id = id;
        }
    }

    @Id
    @Column(name = "no")
    private Long no;

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "score")
    private Integer score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "no", insertable = false, updatable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", insertable = false, updatable = false)
    private Subject subject;

    public Lecture(Integer score) {
        this.score = score;
    }
}