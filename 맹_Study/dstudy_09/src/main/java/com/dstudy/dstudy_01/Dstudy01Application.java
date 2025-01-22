package com.dstudy.dstudy_01;

import java.util.List;

import com.dstudy.dstudy_01.domain.BankAccount;
import com.dstudy.dstudy_01.domain.BillingDetails;
import com.dstudy.dstudy_01.domain.CreditCard;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//@SpringBootApplication
public class Dstudy01Application {

    public static void main(String[] args) {

        //SpringApplication.run(Dstudy01Application.class, args);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("billing");

        // 데이터 추가
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();

            try {
                CreditCard creditCard = new CreditCard("12345", "12", "2020");
                creditCard.setOwner("김철수");
                em.persist(creditCard);
                System.out.println("1건 입력 - 키 값 : " + creditCard.getId());

                BankAccount bankAccount = new BankAccount("yhlee", "kb", "swift");
                bankAccount.setOwner("이영희");
                em.persist(bankAccount);
                System.out.println("1건 입력 - 키 값 : " + bankAccount.getId());

                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }

        // 검색
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();

            try {
                List<CreditCard> cards = em.createQuery("SELECT c FROM CreditCard c ORDER BY c.id", CreditCard.class)
                        .getResultList();
                System.out.println("카드 검색 결과");
                cards.forEach(System.out::println);

                List<BankAccount> accounts = em.createQuery("SELECT b FROM BankAccount b ORDER BY b.id", BankAccount.class)
                        .getResultList();
                System.out.println("계좌 검색 결과");
                accounts.forEach(System.out::println);

                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }

        // BillingDetails를 이용하여 전체 검색
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();

            try {
                List<BillingDetails> billingDetails = em.createQuery("SELECT d FROM BillingDetails d ORDER BY d.id", BillingDetails.class)
                        .getResultList();
                System.out.println("전체 검색 결과");
                billingDetails.forEach(System.out::println);

                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }

        emf.close();
    }

}
