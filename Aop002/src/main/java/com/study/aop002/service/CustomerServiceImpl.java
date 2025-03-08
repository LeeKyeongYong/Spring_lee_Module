package com.study.aop002.service;

import com.study.aop002.dao.CustomerDao;
import com.study.aop002.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import java.util.List;

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
            if(i == when) {
                throw new DataAccessException("예외 발생") {};
            }
            customerDao.addCustomer(customers.get(i));
        }
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerDao.getAllCustomers();
    }
}