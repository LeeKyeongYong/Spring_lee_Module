/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables.daos;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.jooq.Configuration;
import org.jooq.generated.tables.JStudent;
import org.jooq.generated.tables.pojos.Student;
import org.jooq.generated.tables.records.StudentRecord;
import org.jooq.impl.DAOImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class StudentDao extends DAOImpl<StudentRecord, Student, Long> {

    /**
     * Create a new StudentDao without any configuration
     */
    public StudentDao() {
        super(JStudent.STUDENT, Student.class);
    }

    /**
     * Create a new StudentDao with an attached configuration
     */
    public StudentDao(Configuration configuration) {
        super(JStudent.STUDENT, Student.class, configuration);
    }

    @Override
    public Long getId(Student object) {
        return object.getNo();
    }

    /**
     * Fetch records that have <code>birth_date BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<Student> fetchRangeOfJBirthDate(LocalDate lowerInclusive, LocalDate upperInclusive) {
        return fetchRange(JStudent.STUDENT.BIRTH_DATE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>birth_date IN (values)</code>
     */
    public List<Student> fetchByJBirthDate(LocalDate... values) {
        return fetch(JStudent.STUDENT.BIRTH_DATE, values);
    }

    /**
     * Fetch records that have <code>no BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<Student> fetchRangeOfJNo(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(JStudent.STUDENT.NO, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>no IN (values)</code>
     */
    public List<Student> fetchByJNo(Long... values) {
        return fetch(JStudent.STUDENT.NO, values);
    }

    /**
     * Fetch a unique record that has <code>no = value</code>
     */
    public Student fetchOneByJNo(Long value) {
        return fetchOne(JStudent.STUDENT.NO, value);
    }

    /**
     * Fetch a unique record that has <code>no = value</code>
     */
    public Optional<Student> fetchOptionalByJNo(Long value) {
        return fetchOptional(JStudent.STUDENT.NO, value);
    }

    /**
     * Fetch records that have <code>email BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<Student> fetchRangeOfJEmail(String lowerInclusive, String upperInclusive) {
        return fetchRange(JStudent.STUDENT.EMAIL, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>email IN (values)</code>
     */
    public List<Student> fetchByJEmail(String... values) {
        return fetch(JStudent.STUDENT.EMAIL, values);
    }

    /**
     * Fetch records that have <code>name BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<Student> fetchRangeOfJName(String lowerInclusive, String upperInclusive) {
        return fetchRange(JStudent.STUDENT.NAME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>name IN (values)</code>
     */
    public List<Student> fetchByJName(String... values) {
        return fetch(JStudent.STUDENT.NAME, values);
    }
}
