package com.aop004.service;

import com.aop004.domain.Customer;
import java.util.List;
public interface CustomerService {
    void addCustomers(List<Customer> customers, int when);
    List<Customer> getAllCustomers();
}