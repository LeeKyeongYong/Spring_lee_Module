package com.dstudy.dstudy_01;

import com.dstudy.dstudy_01.domain.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.util.GregorianCalendar;
import java.util.List;
import com.dstudy.dstudy_01.domain.*;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//@SpringBootApplication
public class Dstudy01Application {

    public static void main(String[] args) {

        //SpringApplication.run(Dstudy01Application.class, args);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("lecture");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        // Data addition
        tx.begin();

        Student kim = new Student();
        kim.setName("김철수");
        kim.setBirthDate(new GregorianCalendar(2000, 0, 1).getTime());
        kim.setEmail("cskim@gmail.com");

        Student lee = new Student();
        lee.setName("이영희");
        lee.setBirthDate(new GregorianCalendar(2001, 6, 1).getTime());
        lee.setEmail("yhlee@naver.com");

        Subject korean = new Subject();
        korean.setName("국어");
        korean.setHours(3);
        korean.setOpenDate(new GregorianCalendar(2014, 0, 1).getTime());

        Subject english = new Subject();
        english.setName("영어");
        english.setHours(2);
        english.setOpenDate(new GregorianCalendar(2012, 0, 1).getTime());

        kim.addSubject(korean);
        kim.addSubject(english);
        lee.addSubject(english);

        em.persist(kim);
        em.persist(lee);

        tx.commit();
        em.close();

        System.out.println("Data added successfully.");

        // Data retrieval
        em = emf.createEntityManager();
        tx = em.getTransaction();
        tx.begin();

        List<Student> students = em.createQuery("select s from Student s order by s.no", Student.class)
                .getResultList();

        students.forEach(s -> {
            System.out.println("Student:");
            System.out.println("No: " + s.getNo());
            System.out.println("Name: " + s.getName());
            System.out.println("Email: " + s.getEmail());
            System.out.println("Birth Date: " + s.getBirthDate());
            System.out.println("Enrolled Subjects:");
            s.getSubjects().forEach(sub -> {
                System.out.println("ID: " + sub.getId());
                System.out.println("Name: " + sub.getName());
                System.out.println("Hours: " + sub.getHours());
                System.out.println("Open Date: " + sub.getOpenDate());
            });
        });

        tx.commit();
        em.close();
    }

}
