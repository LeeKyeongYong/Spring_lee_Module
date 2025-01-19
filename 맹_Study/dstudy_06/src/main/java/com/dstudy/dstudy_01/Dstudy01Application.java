package com.dstudy.dstudy_01;

import com.dstudy.dstudy_01.domain.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import jakarta.persistence.*;
import java.util.List;

//@SpringBootApplication
public class Dstudy01Application {

    public static void main(String[] args) {
        //SpringApplication.run(Dstudy01Application.class, args);

        // EntityManagerFactory 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("billing");

        // 데이터 추가
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // 카드 1건
            CreditCard creditCard = new CreditCard(null, "12345", "12", "2020");
            creditCard.setOwner("김철수");
            em.persist(creditCard);
            System.out.println("카드 입력 완료 - ID: " + creditCard.getId());

            // 계좌 1건
            BankAccount bankAccount = new BankAccount(null, "yhlee", "kb", "swift");
            bankAccount.setOwner("이영희");
            em.persist(bankAccount);
            System.out.println("계좌 입력 완료 - ID: " + bankAccount.getId());

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }

        // 데이터 조회
        em = emf.createEntityManager();
        tx = em.getTransaction();

        try {
            tx.begin();

            // 카드 검색
            List<CreditCard> cards = em.createQuery("SELECT c FROM CreditCard c ORDER BY c.id", CreditCard.class)
                    .getResultList();
            System.out.println("\n카드 검색 결과:");
            cards.forEach(System.out::println);

            // 계좌 검색
            List<BankAccount> accounts = em.createQuery("SELECT b FROM BankAccount b ORDER BY b.id", BankAccount.class)
                    .getResultList();
            System.out.println("\n계좌 검색 결과:");
            accounts.forEach(System.out::println);

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }

}
