package com.dstudy.dstudy_01;

//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import com.dstudy.dstudy_01.domain1.*;
import java.time.LocalDate;
import java.util.List;

//@SpringBootApplication
public class Dstudy01Application {

    public static void main(String[] args) {
        //SpringApplication.run(Dstudy01Application.class, args);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("member");

        // 데이터 추가
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Member member = new Member();
            member.setId("cskim");
            member.setPassword("cskim1234");
            member.setBirthDate(LocalDate.of(2000, 1, 1));
            member.setEmail("kim@gmail.com");
            member.setPhone("010-2222-3333");

            Address address = new Address();
            address.setPostCode("111-222");
            address.setCity("서울");
            address.setAddress("강남구");
            member.setAddress(address);

            em.persist(member);
            em.getTransaction().commit();
            System.out.println("입력 완료");
        }

        // 검색
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            List<Member> members = em.createQuery("SELECT m FROM Member m ORDER BY m.id", Member.class)
                    .getResultList();

            members.forEach(m -> {
                System.out.println("id: " + m.getId());
                System.out.println("password: " + m.getPassword());
                System.out.println("birthDate: " + m.getBirthDate());
                System.out.println("email: " + m.getEmail());
                System.out.println("phone: " + m.getPhone());

                if (m.getAddress() != null) {
                    System.out.println("postCode: " + m.getAddress().getPostCode());
                    System.out.println("city: " + m.getAddress().getCity());
                    System.out.println("address: " + m.getAddress().getAddress());
                }
            });

            em.getTransaction().commit();
        }

        emf.close();
    }

}
