package com.aopstudy.aop001;

import com.aopstudy.aop001.domain.Customer;
import com.aopstudy.aop001.service.CustomerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataAccessException;

import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

@SpringBootApplication
public class Aop001Application {

    public static void main(String[] args) {
        SpringApplication.run(Aop001Application.class, args);
    }

    @Bean
    public CommandLineRunner run(CustomerService service) {
        return args -> {
            List<Customer> customers = Arrays.asList(
                    new Customer(0, "kim", "kim@naver.com", "010-1111-6666",
                            new GregorianCalendar(1990, 0, 1).getTime()),
                    new Customer(0, "lee", "lee@naver.com", "010-2222-5555",
                            new GregorianCalendar(1991, 1, 1).getTime()),
                    new Customer(0, "kang", "kang@naver.com", "010-3333-4444",
                            new GregorianCalendar(1992, 2, 1).getTime()));

            // 작업 성공
            try {
                service.addCustomers(customers, -1);
            } catch (DataAccessException e) {
                System.out.println("첫 번째 호출 실패: " + e.getMessage());
            }

            System.out.println("작업 성공");
            showAllCustomers(service.getAllCustomers());

            // 작업 실패
            try {
                service.addCustomers(customers, 1);
            } catch (DataAccessException e) {
                System.out.println("작업 실패");
                System.out.println(e.getMessage());
            }

            System.out.println("작업 실패 후");
            showAllCustomers(service.getAllCustomers());
        };
    }

    private static void showAllCustomers(List<Customer> customers) {
        for (Customer c : customers) {
            System.out.println(c);
        }
    }
}
/*
package com.aopstudy.aop001;

import com.aopstudy.aop001.domain.Customer;
import com.aopstudy.aop001.service.CustomerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataAccessException;

import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

@SpringBootApplication
public class Aop001Application {

    public static void main(String[] args) {
        SpringApplication.run(Aop001Application.class, args);
    }
    @Bean
    public CommandLineRunner run(CustomerService service) {
        return args -> {
            List<Customer> customers = Arrays.asList(
                    new Customer(0, "kim", "kim@naver.com", "010-1111-6666",
                            new GregorianCalendar(1990, 0, 1).getTime()),
                    new Customer(0, "lee", "lee@naver.com", "010-2222-5555",
                            new GregorianCalendar(1991, 1, 1).getTime()),
                    new Customer(0, "kang", "kang@naver.com", "010-3333-4444",
                            new GregorianCalendar(1992, 2, 1).getTime()));

            // 작업 성공
            try {
                service.addCustomers(customers, -1);
            } catch (DataAccessException e) {
                System.out.println(e.getMessage());
            }

            showAllCustomers(service.getAllCustomers());

            // 작업 실패
            try {
                service.addCustomers(customers, 1);
            } catch (DataAccessException e) {
                System.out.println(e.getMessage());
            }

            showAllCustomers(service.getAllCustomers());
        };
    }

    private static void showAllCustomers(List<Customer> customers) {
        for (Customer c : customers) {
            System.out.println(c);
        }
    }
}
*/
