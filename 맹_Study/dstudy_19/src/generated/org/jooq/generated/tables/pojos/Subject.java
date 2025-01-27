/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables.pojos;


import java.io.Serializable;
import java.time.LocalDate;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Subject implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer hours;
    private LocalDate openDate;
    private Long id;
    private String name;

    public Subject() {}

    public Subject(Subject value) {
        this.hours = value.hours;
        this.openDate = value.openDate;
        this.id = value.id;
        this.name = value.name;
    }

    public Subject(
        Integer hours,
        LocalDate openDate,
        Long id,
        String name
    ) {
        this.hours = hours;
        this.openDate = openDate;
        this.id = id;
        this.name = name;
    }

    /**
     * Getter for <code>msa.subject.hours</code>.
     */
    public Integer getHours() {
        return this.hours;
    }

    /**
     * Setter for <code>msa.subject.hours</code>.
     */
    public Subject setHours(Integer hours) {
        this.hours = hours;
        return this;
    }

    /**
     * Getter for <code>msa.subject.open_date</code>.
     */
    public LocalDate getOpenDate() {
        return this.openDate;
    }

    /**
     * Setter for <code>msa.subject.open_date</code>.
     */
    public Subject setOpenDate(LocalDate openDate) {
        this.openDate = openDate;
        return this;
    }

    /**
     * Getter for <code>msa.subject.id</code>.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Setter for <code>msa.subject.id</code>.
     */
    public Subject setId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Getter for <code>msa.subject.name</code>.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter for <code>msa.subject.name</code>.
     */
    public Subject setName(String name) {
        this.name = name;
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
        final Subject other = (Subject) obj;
        if (this.hours == null) {
            if (other.hours != null)
                return false;
        }
        else if (!this.hours.equals(other.hours))
            return false;
        if (this.openDate == null) {
            if (other.openDate != null)
                return false;
        }
        else if (!this.openDate.equals(other.openDate))
            return false;
        if (this.id == null) {
            if (other.id != null)
                return false;
        }
        else if (!this.id.equals(other.id))
            return false;
        if (this.name == null) {
            if (other.name != null)
                return false;
        }
        else if (!this.name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.hours == null) ? 0 : this.hours.hashCode());
        result = prime * result + ((this.openDate == null) ? 0 : this.openDate.hashCode());
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Subject (");

        sb.append(hours);
        sb.append(", ").append(openDate);
        sb.append(", ").append(id);
        sb.append(", ").append(name);

        sb.append(")");
        return sb.toString();
    }
}
