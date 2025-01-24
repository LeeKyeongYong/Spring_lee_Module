package com.dstudy.dstudy_01;

//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//@SpringBootApplication

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.util.GregorianCalendar;
import java.util.List;
import com.dstudy.dstudy_01.domain2.*;

public class Dstudy02Application {

    public static void main(String[] args) {
        //SpringApplication.run(Dstudy01Application.class, args);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("member");

        // 데이터 추가
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Member member = new Member();
        member.setId("cskim");
        member.setPassword("cskim1234");
        member.setBirthDate(new GregorianCalendar(2000, 0, 1).getTime());
        member.setEmail("kim@gmail.com");
        member.setPhone("010-2222-3333");

        Address address = new Address();
        address.setPostCode("111-222");
        address.setCity("서울");
        address.setAddress("강남구");
        member.setAddress(address);

        em.persist(member);
        System.out.println("입력 완료");

        tx.commit();
        em.close();

        // 검색
        em = emf.createEntityManager();
        tx = em.getTransaction();
        tx.begin();

        List<Member> members = em.createQuery("select m from Member m order by m.id", Member.class)
                .getResultList();
        for(Member m : members) {
            System.out.println("id : " + m.getId());
            System.out.println("password : " + m.getPassword());
            System.out.println("birthDate : " + m.getBirthDate());
            System.out.println("email : " + m.getEmail());
            System.out.println("phone : " + m.getPhone());
            System.out.println("postCode : " + m.getAddress().getPostCode());
            System.out.println("city : " + m.getAddress().getCity());
            System.out.println("address : " + m.getAddress().getAddress());
        }

        tx.commit();
        em.close();

        emf.close();
    }
}
