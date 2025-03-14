package com.aopstudy.aop001.service;

import com.aopstudy.aop001.dao.CustomerDao;
import com.aopstudy.aop001.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerDao customerDao;

    @Autowired
    public CustomerServiceImpl(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Override
    @Transactional
    public void addCustomers(List<Customer> customers, int when) {
        for (int i = 0; i < customers.size(); i++) {
            if (i == when) {
                throw new DataAccessException("에러 발생") {
                    @Override
                    public String getMessage() {
                        return "에러 발생";
                    }
                };
            }
            customerDao.addCustomer(customers.get(i));
        }
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerDao.getAllCustomers();
    }
}