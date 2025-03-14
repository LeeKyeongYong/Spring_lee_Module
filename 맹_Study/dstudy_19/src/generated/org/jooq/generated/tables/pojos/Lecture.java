/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Lecture implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer score;
    private Long id;
    private Long no;

    public Lecture() {}

    public Lecture(Lecture value) {
        this.score = value.score;
        this.id = value.id;
        this.no = value.no;
    }

    public Lecture(
        Integer score,
        Long id,
        Long no
    ) {
        this.score = score;
        this.id = id;
        this.no = no;
    }

    /**
     * Getter for <code>msa.lecture.score</code>.
     */
    public Integer getScore() {
        return this.score;
    }

    /**
     * Setter for <code>msa.lecture.score</code>.
     */
    public Lecture setScore(Integer score) {
        this.score = score;
        return this;
    }

    /**
     * Getter for <code>msa.lecture.id</code>.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Setter for <code>msa.lecture.id</code>.
     */
    public Lecture setId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Getter for <code>msa.lecture.no</code>.
     */
    public Long getNo() {
        return this.no;
    }

    /**
     * Setter for <code>msa.lecture.no</code>.
     */
    public Lecture setNo(Long no) {
        this.no = no;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Lecture other = (Lecture) obj;
        if (this.score == null) {
            if (other.score != null)
                return false;
        }
        else if (!this.score.equals(other.score))
            return false;
        if (this.id == null) {
            if (other.id != null)
                return false;
        }
        else if (!this.id.equals(other.id))
            return false;
        if (this.no == null) {
            if (other.no != null)
                return false;
        }
        else if (!this.no.equals(other.no))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.score == null) ? 0 : this.score.hashCode());
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.no == null) ? 0 : this.no.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Lecture (");

        sb.append(score);
        sb.append(", ").append(id);
        sb.append(", ").append(no);

        sb.append(")");
        return sb.toString();
    }
}
