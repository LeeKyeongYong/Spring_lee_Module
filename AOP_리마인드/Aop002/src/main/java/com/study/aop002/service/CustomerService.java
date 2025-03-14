package com.study.aop002.service;

import com.study.aop002.model.Customer;

import java.util.List;

public interface CustomerService {
    public void addCustomers(List<Customer> customers, int when);
    public List<Customer> getAllCustomers();
}