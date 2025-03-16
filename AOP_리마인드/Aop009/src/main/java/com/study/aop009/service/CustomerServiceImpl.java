package com.study.aop009.service;

import java.util.List;

import com.study.aop009.dao.CustomerDao;
import com.study.aop009.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {
    private CustomerDao customerDao;

    @Autowired
    public void setCustomerDao(CustomerDao customerDao) {
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