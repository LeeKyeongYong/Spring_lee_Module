package com.proxypstudy.aop005.service;

import java.util.List;

import com.proxypstudy.aop005.dao.CustomerDao;
import com.proxypstudy.aop005.domain.Customer;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerDao customerDao;

    public CustomerServiceImpl(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Transactional
    @Override
    public void addCustomers(List<Customer> customers, int when) {
        for (int i = 0; i < customers.size(); i++) {
            if (i == when) throw new DataAccessException("에러 발생") {};
            customerDao.addCustomer(customers.get(i));
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<Customer> getAllCustomers() {
        return customerDao.getAllCustomers();
    }
}
