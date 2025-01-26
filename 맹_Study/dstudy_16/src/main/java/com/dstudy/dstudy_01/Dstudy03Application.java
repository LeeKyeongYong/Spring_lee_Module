package com.dstudy.dstudy_01;

//@SpringBootApplication

import com.dstudy.dstudy_01.domain3.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.util.GregorianCalendar;
import java.util.List;

public class Dstudy03Application {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("lecture");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // 첫 번째 학생 생성 및 저장
            Student kim = new Student();
            kim.setHak(1);
            kim.setBan(1);
            kim.setBun(1);
            kim.setName("김철수");
            kim.setGender("M");
            kim.setBirthDate(new GregorianCalendar(2001, 0, 1).getTime());
            em.persist(kim);

            // 두 번째 학생 생성 및 저장
            Student lee = new Student();
            lee.setHak(2);
            lee.setBan(5);
            lee.setBun(15);
            lee.setName("이영희");
            lee.setGender("F");
            lee.setBirthDate(new GregorianCalendar(2000, 11, 25).getTime());
            em.persist(lee);

            tx.commit();

            System.out.println("입력 완료");

            // 학생 검색 및 출력
            tx.begin();
            List<Student> students = em.createQuery(
                            "SELECT s FROM Student s ORDER BY s.hak, s.ban, s.bun",
                            Student.class)
                    .getResultList();

            students.forEach(s -> {
                System.out.println("hak : " + s.getHak());
                System.out.println("ban : " + s.getBan());
                System.out.println("bun : " + s.getBun());
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
