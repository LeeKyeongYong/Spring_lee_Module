package com.study.aop002.dao;

import com.study.aop002.model.Customer;

import java.util.List;

public interface CustomerDao {
    public void addCustomer(Customer customer);
    public List<Customer> getAllCustomers();
}
