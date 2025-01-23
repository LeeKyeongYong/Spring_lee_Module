package com.dstudy.dstudy_01;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;
import com.dstudy.dstudy_01.domain.*;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//@SpringBootApplication
public class Dstudy01Application {

    public static void main(String[] args) {

        //SpringApplication.run(Dstudy01Application.class, args);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("memberPersistenceUnit");

        try {
            // 데이터 추가
            addMember(emf);

            // 데이터 조회
            listMembers(emf);
        } finally {
            emf.close();
        }
    }

    private static void addMember(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Map<String, String> phones = new HashMap<>();
            phones.put("office", "010-1111-2222");
            phones.put("home", "010-2222-3333");

            Member member = new Member("김철수", "cskim@gmail.com", phones);
            em.persist(member);

            em.getTransaction().commit();
            System.out.println("1건 입력 - 키 값 : " + member.getId());
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private static void listMembers(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            var members = em.createQuery("SELECT m FROM Member m ORDER BY m.id", Member.class)
                    .getResultList();

            System.out.println("검색 결과");
            members.forEach(m -> {
                System.out.println("id : " + m.getId());
                System.out.println("name : " + m.getName());
                System.out.println("email : " + m.getEmail());

                m.getPhones().forEach((type, number) ->
                        System.out.println("phone (" + type + ") : " + number)
                );
            });

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

}
