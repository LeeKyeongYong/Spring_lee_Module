package com.dstudy.dstudy_01;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.dstudy.dstudy_01.domain.*;
import java.time.LocalDate;
import java.util.List;

//@SpringBootApplication
public class Dstudy01Application {

    private static final Logger logger = LoggerFactory.getLogger(Dstudy01Application.class);

    public static void main(String[] args) {

        //SpringApplication.run(Dstudy01Application.class, args);

        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("member")) {
            insertMember(emf);
            displayAllMembers(emf);
        } catch (Exception e) {
            logger.error("An error occurred: ", e);
        }
    }

    private static void insertMember(EntityManagerFactory emf) {
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();

                Member member = new Member(
                        null,
                        "김철수",
                        LocalDate.of(2013, 1, 1),  // LocalDate 사용
                        "cskim@gmail.com",
                        new Address("111-1111", "제주시", "010-1111-2222"),
                        new Address("222-2222", "서울", "010-2222-3333")
                );

                em.persist(member);
                logger.info("데이터 입력 완료. 기본 키: {}", member.getNo());

                tx.commit();
            } catch (Exception e) {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
                throw e;
            }
        }
    }

    private static void displayAllMembers(EntityManagerFactory emf) {
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();

                List<Member> members = em.createQuery(
                        "SELECT m FROM Member m ORDER BY m.no",
                        Member.class
                ).getResultList();

                logger.info("데이터 출력:");
                members.forEach(m -> logger.info(m.toString()));

                tx.commit();
            } catch (Exception e) {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
                throw e;
            }
        }


    }

}
