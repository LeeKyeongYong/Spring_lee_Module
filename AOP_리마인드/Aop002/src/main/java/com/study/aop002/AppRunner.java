package com.study.aop002;


import com.study.aop002.model.Customer;
import com.study.aop002.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Component
public class AppRunner implements CommandLineRunner {

    private final CustomerService customerService;

    @Autowired
    public AppRunner(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public void run(String... args) {
        List<Customer> customers = Arrays.asList(
                new Customer(0, "jaja", "jaja@naver.com", "010-1111-6666", new GregorianCalendar(1990, Calendar.JANUARY, 1).getTime()),
                new Customer(0, "lee", "lee@naver.com", "010-2222-5555", new GregorianCalendar(1991, Calendar.FEBRUARY, 1).getTime()),
                new Customer(0, "kang", "kang@naver.com", "010-3333-4444", new GregorianCalendar(1992, Calendar.MARCH, 1).getTime())
        );

        // 성공 케이스
        try {
            customerService.addCustomers(customers, 999); // 예외 발생 안함
            System.out.println(">> 고객 추가 성공");
        } catch (Exception e) {
            System.out.println(">> 에러: " + e.getMessage());
        }

        showAllCustomers(customerService.getAllCustomers());

        // 실패 케이스
        try {
            customerService.addCustomers(customers, 1); // 예외 발생
            System.out.println(">> 고객 추가 성공");
        } catch (Exception e) {
            System.out.println(">> 에러: " + e.getMessage());
        }

        showAllCustomers(customerService.getAllCustomers());
    }

    private void showAllCustomers(List<Customer> customers) {
        System.out.println("===== 전체 고객 목록 =====");
        customers.forEach(System.out::println);
        System.out.println("========================");
    }
}