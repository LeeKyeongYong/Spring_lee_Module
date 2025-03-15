package com.study.aop009.service;

import com.study.aop009.domain.Customer;
import java.util.List;

public interface CustomerService {
    public void addCustomers(List<Customer> customers, int when);
    public List<Customer> getAllCustomers();
}