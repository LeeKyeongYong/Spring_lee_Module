package com.study.aop003.service;

import com.study.aop003.domain.Customer;
import java.util.List;

public interface CustomerService {
    void addCustomers(List<Customer> customers, int when);
    List<Customer> getAllCustomers();
}
