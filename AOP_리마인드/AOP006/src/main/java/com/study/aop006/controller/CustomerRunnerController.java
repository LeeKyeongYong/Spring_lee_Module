package com.study.aop006.controller;

import com.study.aop006.domain.Customer;
import com.study.aop006.service.CustomerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

@Component
public class CustomerRunnerController implements CommandLineRunner {

    private final CustomerService customerService;

    public CustomerRunnerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Customer> customers = Arrays.asList(
                new Customer(0, "kim", "kim@naver.com", "010-1111-6666", new GregorianCalendar(1990, 0, 1).getTime()),
                new Customer(0, "lee", "lee@naver.com", "010-2222-5555", new GregorianCalendar(1991, 1, 1).getTime()),
                new Customer(0, "kang", "kang@naver.com", "010-3333-4444", new GregorianCalendar(1992, 2, 1).getTime()));

        // Successful operation
        try {
            customerService.addCustomers(customers, -1);
            System.out.println("고객이 성공적으로 추가되었습니다.");
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }

        showAllCustomers(customerService.getAllCustomers());

        // Failed operation
        try {
            customerService.addCustomers(customers, 1);
        } catch (DataAccessException e) {
            System.out.println("트랜잭션이 롤백되었습니다.: " + e.getMessage());
        }

        showAllCustomers(customerService.getAllCustomers());
    }

    private void showAllCustomers(List<Customer> customers) {
        System.out.println("--- 모든 고객 보여드립니다. ---");
        for(Customer c : customers) {
            System.out.println(c);
        }
        System.out.println("--------------------");
    }
}