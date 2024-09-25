package com.jstudy.hibernate2.domain.dao;

import com.jstudy.hibernate2.domain.entity.Student;
import com.jstudy.hibernate2.domain.entity.Subject;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class EnrollDAOImpl implements EnrollDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<Student> getAllStudents() {
        TypedQuery<Student> query = entityManager.createQuery(
                "SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.subjects ORDER BY s.id ASC", Student.class);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Subject> getAllSubjects() {
        TypedQuery<Subject> query = entityManager.createQuery(
                "SELECT DISTINCT s FROM Subject s LEFT JOIN FETCH s.students ORDER BY s.no ASC", Subject.class);
        return query.getResultList();
    }

    @Override
    @Transactional
    public void saveStudent(Student student) {
        entityManager.merge(student);
    }

    @Override
    @Transactional
    public void saveSubject(Subject subject) {
        entityManager.merge(subject);
    }

    @Override
    @Transactional
    public void removeStudent(Student student) {
        entityManager.remove(entityManager.contains(student) ? student : entityManager.merge(student));
    }

    @Override
    @Transactional
    public void removeSubject(Subject subject) {
        entityManager.remove(entityManager.contains(subject) ? subject : entityManager.merge(subject));
    }

    @Override
    @Transactional
    public void removeAll() {
        entityManager.createQuery("DELETE FROM enroll").executeUpdate();
        entityManager.createQuery("DELETE FROM Student").executeUpdate();
        entityManager.createQuery("DELETE FROM Subject").executeUpdate();
    }

    @Override
    @Transactional(readOnly = true)
    public Student getStudentByName(String name) {
        TypedQuery<Student> query = entityManager.createQuery(
                "SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.subjects WHERE s.name = :name", Student.class);
        query.setParameter("name", name);
        return query.getSingleResult();
    }

    @Override
    @Transactional(readOnly = true)
    public Subject getSubjectByName(String name) {
        TypedQuery<Subject> query = entityManager.createQuery(
                "SELECT DISTINCT s FROM Subject s LEFT JOIN FETCH s.students WHERE s.name = :name", Subject.class);
        query.setParameter("name", name);
        return query.getSingleResult();
    }
}