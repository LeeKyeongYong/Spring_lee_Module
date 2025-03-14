package com.example.aop007.service;

import java.util.List;

import com.example.aop007.dao.CustomerDao;
import com.example.aop007.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service("customerService")
public class CustomerServiceImpl implements CustomerService {
    private final CustomerDao customerDao;

    @Autowired
    public CustomerServiceImpl(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Override
    public void addCustomers(List<Customer> customers, int when) {
        for (int i = 0; i < customers.size(); i++) {
            if (i == when) {
                throw new DataAccessException("에러 발생") {};
            }
            customerDao.addCustomer(customers.get(i));
        }
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerDao.getAllCustomers();
    }
}