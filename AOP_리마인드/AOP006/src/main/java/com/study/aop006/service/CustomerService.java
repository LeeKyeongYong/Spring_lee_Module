package com.study.aop006.service;

import com.study.aop006.domain.Customer;
import java.util.List;
public interface CustomerService {
    void addCustomers(List<Customer> customers, int when);
    List<Customer> getAllCustomers();
}