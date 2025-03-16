package com.study.aop010.service;

import com.study.aop010.domain.Customer;

import java.util.List;
public interface CustomerService {
    public void addCustomers(List<Customer> customers, int when);
    public List<Customer> getAllCustomers();
}