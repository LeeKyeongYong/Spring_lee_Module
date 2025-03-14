package com.aop004.service;

import java.util.List;

import com.aop004.dao.CustomerDao;
import com.aop004.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerDao customerDao;

    @Autowired
    public CustomerServiceImpl(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Override
    public void addCustomers(List<Customer> customers, int when) {
        for(int i=0; i<customers.size(); i++) {
            if(i == when) throw new DataAccessException("에러 발생") {};
            customerDao.addCustomer(customers.get(i));
        }
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerDao.getAllCustomers();
    }
}