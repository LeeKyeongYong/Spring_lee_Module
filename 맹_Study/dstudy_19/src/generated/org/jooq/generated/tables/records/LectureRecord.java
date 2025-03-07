/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables.records;


import org.jooq.Record2;
import org.jooq.generated.tables.JLecture;
import org.jooq.generated.tables.pojos.Lecture;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class LectureRecord extends UpdatableRecordImpl<LectureRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>msa.lecture.score</code>.
     */
    public LectureRecord setScore(Integer value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>msa.lecture.score</code>.
     */
    public Integer getScore() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>msa.lecture.id</code>.
     */
    public LectureRecord setId(Long value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>msa.lecture.id</code>.
     */
    public Long getId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>msa.lecture.no</code>.
     */
    public LectureRecord setNo(Long value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>msa.lecture.no</code>.
     */
    public Long getNo() {
        return (Long) get(2);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record2<Long, Long> key() {
        return (Record2) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached LectureRecord
     */
    public LectureRecord() {
        super(JLecture.LECTURE);
    }

    /**
     * Create a detached, initialised LectureRecord
     */
    public LectureRecord(Integer score, Long id, Long no) {
        super(JLecture.LECTURE);

        setScore(score);
        setId(id);
        setNo(no);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised LectureRecord
     */
    public LectureRecord(Lecture value) {
        super(JLecture.LECTURE);

        if (value != null) {
            setScore(value.getScore());
            setId(value.getId());
            setNo(value.getNo());
            resetChangedOnNotNull();
        }
    }
}
