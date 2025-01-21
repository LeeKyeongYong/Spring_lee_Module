package com.dstudy.dstudy_01;

//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//@SpringBootApplication
import jakarta.persistence.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import com.dstudy.dstudy_01.domain.*;
import org.springframework.boot.SpringApplication;

import java.util.List;

public class Dstudy01Application {

    public static void main(String[] args) {
        SpringApplication.run(Dstudy01Application.class, args);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("billing");

        try {
            // 데이터 추가
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();

            CreditCard creditCard = CreditCard.builder()
                    .number("12345")
                    .expMonth("12")
                    .expYear("2020")
                    .build();
            creditCard.setOwner("김철수");
            em.persist(creditCard);
            System.out.println("1건 입력 - 키 값 : " + creditCard.getId());

            BankAccount bankAccount = BankAccount.builder()
                    .account("yhlee")
                    .bankname("kb")
                    .swift("swift")
                    .build();
            bankAccount.setOwner("이영희");
            em.persist(bankAccount);
            System.out.println("1건 입력 - 키 값 : " + bankAccount.getId());

            em.getTransaction().commit();
            em.close();

            // 검색
            em = emf.createEntityManager();
            em.getTransaction().begin();

            List<CreditCard> cards = em.createQuery(
                            "SELECT c FROM CreditCard c ORDER BY c.id", CreditCard.class)
                    .getResultList();
            System.out.println("카드 검색 결과");
            cards.forEach(System.out::println);

            List<BankAccount> accounts = em.createQuery(
                            "SELECT b FROM BankAccount b ORDER BY b.id", BankAccount.class)
                    .getResultList();
            System.out.println("계좌 검색 결과");
            accounts.forEach(System.out::println);

            em.getTransaction().commit();
            em.close();

        } finally {
            emf.close();
        }
    }
}
