package com.dstudy.dstudy_01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//@SpringBootApplication
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.util.GregorianCalendar;
import java.util.List;
import com.dstudy.dstudy_01.domain1.*;
public class Dstudy01Application {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("lecture");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Student.Hakbun kimHakbun = new Student.Hakbun(1, 1, 1);
            Student kim = new Student();
            kim.setHakbun(kimHakbun);
            kim.setName("김철수");
            kim.setGender("M");
            kim.setBirthDate(new GregorianCalendar(2001, 0, 1).getTime());
            em.persist(kim);

            Student.Hakbun leeHakbun = new Student.Hakbun(2, 5, 15);
            Student lee = new Student();
            lee.setHakbun(leeHakbun);
            lee.setName("이영희");
            lee.setGender("F");
            lee.setBirthDate(new GregorianCalendar(2000, 11, 25).getTime());
            em.persist(lee);

            tx.commit();

            System.out.println("입력 완료");

            tx.begin();

            List<Student> students = em.createQuery(
                            "SELECT s FROM Student s ORDER BY s.hakbun.hak, s.hakbun.ban, s.hakbun.bun",
                            Student.class)
                    .getResultList();

            students.forEach(s -> {
                System.out.println("hak : " + s.getHakbun().getHak());
                System.out.println("ban : " + s.getHakbun().getBan());
                System.out.println("bun : " + s.getHakbun().getBun());
                System.out.println("name : " + s.getName());
                System.out.println("gender : " + s.getGender());
                System.out.println("birthDate : " + s.getBirthDate());
                System.out.println("---");
            });

            tx.commit();

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }

    }
}
