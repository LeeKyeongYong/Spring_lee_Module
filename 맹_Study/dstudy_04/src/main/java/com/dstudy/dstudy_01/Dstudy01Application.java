package com.dstudy.dstudy_01;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.dstudy.dstudy_01.domain.Member;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class Dstudy01Application {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("member");

        try {

            createMember(emf, "김철수", 20, LocalDate.of(2000, 1, 1));

            List<Member> members = findAllMembers(emf);
            members.forEach(System.out::println);

        } finally {
            emf.close();
        }
    }

    private static void createMember(EntityManagerFactory emf, String name, int age, LocalDate birthDate) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Member member = new Member(name, age, birthDate);
            em.persist(member);

            tx.commit();
            System.out.println("Created member with ID: " + member.getNo());

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    private static Member findMember(EntityManagerFactory emf, Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Member.class, id);
        } finally {
            em.close();
        }
    }

    private static List<Member> findAllMembers(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT m FROM Member m ORDER BY m.no", Member.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
