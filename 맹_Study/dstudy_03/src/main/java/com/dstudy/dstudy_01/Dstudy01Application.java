package com.dstudy.dstudy_01;

import com.dstudy.dstudy_01.domain.Message;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

//@SpringBootApplication
public class Dstudy01Application {

    public static void main(String[] args) {
        // EntityManagerFactory 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("message");

        try {
            // 첫 번째 작업 단위
            EntityManager em = emf.createEntityManager();
            EntityTransaction tx = em.getTransaction();

            try {
                tx.begin();

                Message message = new Message("안녕하세요.");
                em.persist(message);

                tx.commit();
            } catch (Exception e) {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
                throw e;
            } finally {
                em.close();
            }

            // 두 번째 작업 단위
            em = emf.createEntityManager();
            tx = em.getTransaction();

            try {
                tx.begin();

                List<Message> messages = em.createQuery("SELECT m FROM Message m ORDER BY m.text ASC", Message.class)
                        .getResultList();

                System.out.println(messages.size() + "개의 메시지가 검색되었습니다.");

                for (Message m : messages) {
                    System.out.println(m.getText());
                }

                tx.commit();
            } catch (Exception e) {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
                throw e;
            } finally {
                em.close();
            }

        } finally {
            emf.close();
        }
    }

}
