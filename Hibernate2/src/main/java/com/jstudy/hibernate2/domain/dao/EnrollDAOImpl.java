package com.jstudy.hibernate2.domain.dao;

import com.jstudy.hibernate2.domain.entity.Student;
import com.jstudy.hibernate2.domain.entity.Subject;
import jakarta.annotation.Resource;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("enrollDAO")
@Transactional
public class EnrollDAOImpl implements  EnrollDAO{
    @Resource
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Student> getAllStudents() {
        return sessionFactory.getCurrentSession()
                .createQuery("select distinct s from Student s left join fetch s.subjects order by s.id asc", Student.class)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Subject> getAllSubjects() {
        return sessionFactory.getCurrentSession()
                .createQuery("select distinct s from Subject s left join fetch s.students order by s.no asc", Subject.class)
                .getResultList();
    }

    @Transactional(readOnly = true)
    public List<Student> getAllStudentsWithSubjects() {
        return null;
    }

    @Transactional
    public void saveStudent(Student student) {
        sessionFactory.getCurrentSession().saveOrUpdate(student);
    }

    @Transactional
    public void saveSubject(Subject subject) {
        sessionFactory.getCurrentSession().saveOrUpdate(subject);
    }

    @Transactional
    public void removeStudent(Student student) {
        sessionFactory.getCurrentSession().delete(student);
    }

    public void removeSubject(Subject subject) {
        sessionFactory.getCurrentSession().delete(subject);
    }

    public void removeAll() {
        sessionFactory.getCurrentSession().createNativeQuery("delete from enroll").executeUpdate();
        sessionFactory.getCurrentSession().createQuery("delete from Student").executeUpdate();
        sessionFactory.getCurrentSession().createQuery("delete from Subject").executeUpdate();
    }

    public Student getStudentByName(String name) {
        return sessionFactory.getCurrentSession()
                .createQuery("select distinct s from Student s left join fetch s.subjects where s.name=:name", Student.class)
                .setParameter("name", name)
                .uniqueResult();
    }

    public Subject getSubjectByName(String name) {
        return sessionFactory.getCurrentSession()
                .createQuery("select distinct s from Subject s left join fetch s.students where s.name=:name", Subject.class)
                .setParameter("name", name)
                .uniqueResult();
    }
}