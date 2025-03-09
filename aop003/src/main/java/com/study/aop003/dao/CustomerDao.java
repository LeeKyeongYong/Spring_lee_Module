package com.study.aop003.dao;

import com.study.aop003.domain.Customer;
import java.util.List;
public interface CustomerDao {
    void addCustomer(Customer customer);
    List<Customer> getAllCustomers();
}