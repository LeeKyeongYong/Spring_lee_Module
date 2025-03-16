package com.study.aop008.controller;

import com.study.aop008.domain.Customer;
import com.study.aop008.service.CustomerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

@Component
public class DemoRunnerController implements CommandLineRunner {

    private final CustomerService customerService;

    public DemoRunnerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public void run(String... args) {
        List<Customer> customers = Arrays.asList(
                new Customer(0, "kim", "kim@naver.com", "010-1111-6666", new GregorianCalendar(1990, 0, 1).getTime()),
                new Customer(0, "lee", "lee@naver.com", "010-2222-5555", new GregorianCalendar(1991, 1, 1).getTime()),
                new Customer(0, "kang", "kang@naver.com", "010-3333-4444", new GregorianCalendar(1992, 2, 1).getTime())
        );

        System.out.println("========== 첫 번째 실행 (실패 케이스) ==========");
        try {
            customerService.addCustomers(customers, 1);
        } catch (DataAccessException e) {
            System.out.println("오류 발생: " + e.getMessage());
        }

        showAllCustomers(customerService.getAllCustomers());

        System.out.println("========== 두 번째 실행 (성공 케이스) ==========");
        try {
            customerService.addCustomers(customers, -1);
        } catch (DataAccessException e) {
            System.out.println("오류 발생: " + e.getMessage());
        }

        showAllCustomers(customerService.getAllCustomers());
    }

    private void showAllCustomers(List<Customer> customers) {
        System.out.println("현재 고객 목록:");
        customers.forEach(System.out::println);
        System.out.println("===========================================");
    }
}