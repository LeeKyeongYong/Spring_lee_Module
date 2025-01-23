package com.dstudy.dstudy_01;

//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//@SpringBootApplication
import com.dstudy.dstudy_01.domain.Phone;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.dstudy.dstudy_01.domain.*;
public class Dstudy01Application {

    public static void main(String[] args) {

        //SpringApplication.run(Dstudy01Application.class, args);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("member-persistence-unit");

        try {
            // 데이터 추가
            persistMember(emf);

            // 데이터 조회
            retrieveMembers(emf);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            emf.close();
        }
    }

    private static void persistMember(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Set<Phone> phones = new HashSet<>(Set.of(
                    new Phone("010-1111-2222", "010-1111-2223", "010-1111-2224"),
                    new Phone("010-2222-3333", "010-2222-3334", "010-2222-3335")
            ));

            Member member = new Member("김철수", "cskim@gmail.com", phones);
            em.persist(member);
            System.out.println("1건 입력 - 키 값 : " + member.getId());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private static void retrieveMembers(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            List<Member> members = em.createQuery(
                            "SELECT m FROM Member m ORDER BY m.id", Member.class)
                    .getResultList();

            System.out.println("검색 결과");
            members.forEach(m -> {
                System.out.println("id: " + m.getId());
                System.out.println("name: " + m.getName());
                System.out.println("email: " + m.getEmail());

                m.getPhones().forEach(p -> {
                    System.out.println("home: " + p.home());
                    System.out.println("office: " + p.office());
                    System.out.println("fax: " + p.fax());
                });
            });

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
