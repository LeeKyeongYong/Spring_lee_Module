package com.study.aop008.service;

import com.study.aop008.domain.Customer;

import java.util.List;

public interface CustomerService {
    void addCustomers(List<Customer> customers, int when);
    List<Customer> getAllCustomers();
}