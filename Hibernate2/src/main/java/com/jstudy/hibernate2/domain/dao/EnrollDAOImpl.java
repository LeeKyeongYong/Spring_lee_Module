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
        // TODO Auto-generated method stub
        return sessionFactory.getCurrentSession()
                .createQuery("select distinct s from Student s left join fetch s.subjects order by s.id asc").list();
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Subject> getAllSubjects() {
        // TODO Auto-generated method stub
        return sessionFactory.getCurrentSession()
                .createQuery("select distinct s from Subject s left join fetch s.students order by s.no asc").list();
    }

    @Transactional(readOnly = true)
    public List<Student> getAllStudentsWithSubjects() {
        // TODO Auto-generated method stub
        return null;
    }

    @Transactional
    public void saveStudent(Student student) {
        // TODO Auto-generated method stub
        sessionFactory.getCurrentSession().saveOrUpdate(student);

    }
    @Transactional
    public void saveSubject(Subject subject) {
        // TODO Auto-generated method stub
        sessionFactory.getCurrentSession().saveOrUpdate(subject);
    }

    @Transactional
    public void removeStudent(Student student) {
        // TODO Auto-generated method stub
        sessionFactory.getCurrentSession().delete(student);
    }

    public void removeSubject(Subject subject) {
        // TODO Auto-generated method stub
        sessionFactory.getCurrentSession().delete(subject);
    }

    public void removeAll() {
        // TODO Auto-generated method stub
        sessionFactory.getCurrentSession().createSQLQuery("delete from enroll").executeUpdate();
        sessionFactory.getCurrentSession().createQuery("delete from Student").executeUpdate();
        sessionFactory.getCurrentSession().createQuery("delete from Subject").executeUpdate();
    }

    public Student getStudentByName(String name) {
        // TODO Auto-generated method stub
        return (Student)sessionFactory.getCurrentSession()
                .createQuery("select distinct s from Student s left join fetch s.subjects where s.name=:name")
                .setString("name", name)
                .uniqueResult();
    }

    public Subject getSubjectByName(String name) {
        // TODO Auto-generated method stub
        return (Subject)sessionFactory.getCurrentSession()
                .createQuery("select distinct s from Subject s left join fetch s.students where s.name=:name")
                .setString("name", name)
                .uniqueResult();
    }

}
