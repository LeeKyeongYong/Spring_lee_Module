package com.jstudy.hibernate2.domain.dao;

import com.jstudy.hibernate2.domain.entity.Student;
import com.jstudy.hibernate2.domain.entity.Subject;

import java.util.List;

public interface EnrollDAO {
    public List<Student> getAllStudents();
    public List<Subject> getAllSubjects();
    public Student getStudentByName(String name);
    public Subject getSubjectByName(String name);
    public void saveStudent(Student student);
    public void saveSubject(Subject subject);
    public void removeStudent(Student student);
    public void removeSubject(Subject subject);
    public void removeAll();
}

