package com.example.aop007.service;

import com.example.aop007.domain.Customer;

import java.util.List;

public interface CustomerService {
    void addCustomers(List<Customer> customers, int when);
    List<Customer> getAllCustomers();
}