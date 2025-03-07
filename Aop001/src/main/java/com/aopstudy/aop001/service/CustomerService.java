package com.aopstudy.aop001.service;

import com.aopstudy.aop001.domain.Customer;
import java.util.List;
public interface CustomerService {
    public void addCustomers(List<Customer> customers, int when);
    public List<Customer> getAllCustomers();
}