package com.aop004.dao;

import com.aop004.domain.Customer;

import java.util.List;

public interface CustomerDao {
    void addCustomer(Customer customer);
    List<Customer> getAllCustomers();
}
