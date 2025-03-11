package com.proxypstudy.aop005.service;

import com.proxypstudy.aop005.domain.Customer;
import java.util.List;
public interface CustomerService {
    void addCustomers(List<Customer> customers, int when);
    List<Customer> getAllCustomers();
}