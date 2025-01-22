package com.dstudy.dstudy_01;

//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//@SpringBootApplication
import com.dstudy.dstudy_01.domain.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.Arrays;
import java.util.List;

public class Dstudy01Application {

    public static void main(String[] args) {

        //SpringApplication.run(Dstudy01Application.class, args);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("member");

        // Insert data
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Member member = new Member(
                    null,
                    "김철수",
                    "cskim@gmail.com",
                    Arrays.asList("010-1111-2222", "010-2222-3333")
            );

            em.persist(member);
            System.out.println("Inserted record - ID: " + member.getId());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        // Query data
        em = emf.createEntityManager();
        tx = em.getTransaction();

        try {
            tx.begin();

            List<Member> members = em.createQuery(
                            "SELECT m FROM Member m ORDER BY m.id", Member.class)
                    .getResultList();

            System.out.println("Query Results:");
            members.forEach(m -> {
                System.out.println("ID: " + m.getId());
                System.out.println("Name: " + m.getName());
                System.out.println("Email: " + m.getEmail());
                m.getPhone().forEach(phone ->
                        System.out.println("Phone: " + phone));
                System.out.println("-------------------");
            });

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }

    }
}
