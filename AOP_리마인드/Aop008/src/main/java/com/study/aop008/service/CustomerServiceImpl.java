package com.study.aop008.service;

import com.study.aop008.domain.Customer;
import com.study.aop008.repository.CustomerRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    public void addCustomers(List<Customer> customers, int when) {
        for (int i = 0; i < customers.size(); i++) {
            if (i == when) {
                throw new DataAccessException("에러 발생") {};
            }
            customerRepository.save(customers.get(i));
        }
    }

    @Override
    @Transactional(readOnly = true, timeout = 30)
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}