package com.dstudy.dstudy_01;

import com.dstudy.dstudy_01.domain.*;
import jakarta.persistence.*;
import java.util.List;

public class Dstudy01Application {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("billing");

        try {
            // 데이터 추가
            EntityManager em = emf.createEntityManager();
            EntityTransaction tx = em.getTransaction();

            try {
                tx.begin();

                CreditCard creditCard = new CreditCard("12345", "12", "2020");
                creditCard.setOwner("김철수");
                em.persist(creditCard);
                System.out.println("Credit Card 입력 - 키 값 : " + creditCard.getId());

                BankAccount bankAccount = new BankAccount("yhlee", "kb", "swift");
                bankAccount.setOwner("이영희");
                em.persist(bankAccount);
                System.out.println("Bank Account 입력 - 키 값 : " + bankAccount.getId());

                tx.commit();
            } catch (Exception e) {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
                throw e;
            } finally {
                em.close();
            }

            // 검색
            em = emf.createEntityManager();
            tx = em.getTransaction();

            try {
                tx.begin();

                // CreditCard만 검색
                List<CreditCard> cards = em.createQuery(
                                "SELECT c FROM CreditCard c", CreditCard.class)
                        .getResultList();

                System.out.println("\n카드 검색 결과:");
                for (CreditCard card : cards) {
                    System.out.println(card);
                }

                // BankAccount만 검색
                List<BankAccount> accounts = em.createQuery(
                                "SELECT b FROM BankAccount b", BankAccount.class)
                        .getResultList();

                System.out.println("\n계좌 검색 결과:");
                for (BankAccount account : accounts) {
                    System.out.println(account);
                }

                // BillingDetails 전체 검색
                List<BillingDetails> billingDetails = em.createQuery(
                                "SELECT d FROM BillingDetails d", BillingDetails.class)
                        .getResultList();

                System.out.println("\n전체 검색 결과:");
                for (BillingDetails detail : billingDetails) {
                    if (detail instanceof CreditCard) {
                        CreditCard card = (CreditCard) detail;
                        System.out.println("Credit Card: " + card);
                    } else if (detail instanceof BankAccount) {
                        BankAccount account = (BankAccount) detail;
                        System.out.println("Bank Account: " + account);
                    }
                }

                tx.commit();
            } catch (Exception e) {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
                e.printStackTrace();
            } finally {
                em.close();
            }

        } finally {
            emf.close();
        }
    }
}