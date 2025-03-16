package com.study.aop008.repository;

import com.study.aop008.domain.Customer;

import java.util.List;

public interface CustomerRepository {
    void save(Customer customer);
    List<Customer> findAll();
}