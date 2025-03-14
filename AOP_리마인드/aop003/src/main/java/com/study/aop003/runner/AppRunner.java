package com.study.aop003.runner;

import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

import com.study.aop003.domain.Customer;
import com.study.aop003.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
@Component
public class AppRunner implements CommandLineRunner {

    private final CustomerService customerService;

    @Autowired
    public AppRunner(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Customer> customers = Arrays.asList(
                new Customer(0, "kim", "kim@naver.com", "010-1111-6666", new GregorianCalendar(1990, 0, 1).getTime()),
                new Customer(0, "lee", "lee@naver.com", "010-2222-5555", new GregorianCalendar(1991, 1, 1).getTime()),
                new Customer(0, "kang", "kang@naver.com", "010-3333-4444", new GregorianCalendar(1992, 2, 1).getTime()));

        // 작업 성공
        try {
            customerService.addCustomers(customers, -1);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }

        showAllCustomers(customerService.getAllCustomers());

        // 작업 실패
        try {
            customerService.addCustomers(customers, 1);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }

        showAllCustomers(customerService.getAllCustomers());
    }

    private void showAllCustomers(List<Customer> customers) {
        for(Customer c : customers) {
            System.out.println(c);
        }
    }
}