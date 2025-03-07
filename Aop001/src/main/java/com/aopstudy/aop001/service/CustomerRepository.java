package com.aopstudy.aop001.dao;

import com.aopstudy.aop001.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}