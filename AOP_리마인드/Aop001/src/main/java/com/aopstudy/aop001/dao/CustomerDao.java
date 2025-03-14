package com.aopstudy.aop001.dao;

import com.aopstudy.aop001.domain.Customer;
import java.util.List;

public interface CustomerDao {
    public void addCustomer(Customer customer);
    public List<Customer> getAllCustomers();
}
