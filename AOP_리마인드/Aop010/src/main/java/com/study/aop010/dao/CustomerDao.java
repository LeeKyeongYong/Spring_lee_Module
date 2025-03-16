package com.study.aop010.dao;

import com.study.aop010.domain.Customer;

import java.util.List;
public interface CustomerDao {
    public void addCustomer(Customer customer);
    public List<Customer> getAllCustomers();
}