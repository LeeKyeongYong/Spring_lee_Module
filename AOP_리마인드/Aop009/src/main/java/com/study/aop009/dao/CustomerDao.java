package com.study.aop009.dao;

import com.study.aop009.domain.Customer;

import java.util.List;

public interface CustomerDao {
    public void addCustomer(Customer customer);
    public List<Customer> getAllCustomers();
}