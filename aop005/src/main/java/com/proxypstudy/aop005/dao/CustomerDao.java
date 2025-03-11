package com.proxypstudy.aop005.dao;

import com.proxypstudy.aop005.domain.Customer;
import java.util.List;

public interface CustomerDao {
    void addCustomer(Customer customer);
    List<Customer> getAllCustomers();
}