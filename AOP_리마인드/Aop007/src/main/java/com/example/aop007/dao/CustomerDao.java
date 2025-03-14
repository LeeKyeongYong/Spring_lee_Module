package com.example.aop007.dao;

import com.example.aop007.domain.Customer;

import java.util.List;

public interface CustomerDao {
    void addCustomer(Customer customer);
    List<Customer> getAllCustomers();
}