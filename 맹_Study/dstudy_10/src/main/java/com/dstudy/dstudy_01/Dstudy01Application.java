package com.dstudy.dstudy_01;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sun.tools.javac.Main;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import com.dstudy.dstudy_01.domain.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//@SpringBootApplication
public class Dstudy01Application {
    private static final Logger logger = LoggerFactory.getLogger(Dstudy01Application.class);

    public static void main(String[] args) {

        //SpringApplication.run(Dstudy01Application.class, args);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("member");

        // Insert data
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();

                Set<String> phones = new HashSet<>();
                phones.add("010-1111-2222");
                phones.add("010-2222-3333");

                Member member = new Member(null, "김철수", "cskim@gmail.com", phones, LocalDate.now());
                em.persist(member);
                logger.info("Inserted 1 record - ID: {}", member.getId());

                tx.commit();
            } catch (Exception e) {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
                logger.error("Error during insert: ", e);
                throw e;
            }
        }

        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();

                List<Member> members = em.createQuery("SELECT m FROM Member m ORDER BY m.id", Member.class)
                        .getResultList();

                logger.info("Query Results:");
                members.forEach(m -> {
                    logger.info("ID: {}", m.getId());
                    logger.info("Name: {}", m.getName());
                    logger.info("Email: {}", m.getEmail());
                    m.getPhone().forEach(phone ->
                            logger.info("Phone: {}", phone)
                    );
                });

                tx.commit();
            } catch (Exception e) {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
                logger.error("Error during query: ", e);
                throw e;
            }
        } finally {
            emf.close();
        }





    }
}
