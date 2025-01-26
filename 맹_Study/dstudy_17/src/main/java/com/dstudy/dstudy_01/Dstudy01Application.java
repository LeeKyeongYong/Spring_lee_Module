package com.dstudy.dstudy_01;

import com.dstudy.dstudy_01.domain.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.util.GregorianCalendar;
import java.util.List;
import com.dstudy.dstudy_01.domain.*;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//@SpringBootApplication
public class Dstudy01Application {

    public static void main(String[] args) {

        //SpringApplication.run(Dstudy01Application.class, args);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("employee");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Employee kim = new Employee("김철수", "cskim@gmail.com", "010-2222-3333");

            Family one = new Family(
                    new GregorianCalendar(2013, 0, 1).getTime(),
                    "김첫째",
                    "남"
            );
            one.setId(1L);
            kim.addFamily(one);

            Family two = new Family(
                    new GregorianCalendar(2014, 0, 1).getTime(),
                    "김둘째",
                    "여"
            );
            two.setId(2L);
            kim.addFamily(two);

            em.persist(kim);

            tx.commit();

            System.out.println("입력 완료");

            // 검색
            tx.begin();

            List<Employee> employees = em.createQuery(
                    "SELECT e FROM Employee e ORDER BY e.id",
                    Employee.class
            ).getResultList();

            employees.forEach(e -> {
                System.out.println("id : " + e.getId());
                System.out.println("name : " + e.getName());
                System.out.println("email : " + e.getEmail());
                System.out.println("phone : " + e.getPhone());

                e.getFamilies().forEach(f -> {
                    System.out.println("name : " + f.getName());
                    System.out.println("birthDate : " + f.getBirthDate());
                    System.out.println("gender : " + f.getGender());
                });
            });

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}
