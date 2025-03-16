package com.study.aop010.service;

import java.util.List;

import com.study.aop010.dao.CustomerDao;
import com.study.aop010.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private CustomerDao customerDao;

    @Autowired
    public void setCustomerDao(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Override
    @Transactional
    public void addCustomers(List<Customer> customers, int when) {
        for(int i=0; i<customers.size(); i++) {
            if(i == when) throw new DataAccessException("에러 발생") {};
            customerDao.addCustomer(customers.get(i));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return customerDao.getAllCustomers();
    }
}