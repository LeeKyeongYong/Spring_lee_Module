package com.study.aop006.service;

import java.util.List;
import com.study.aop006.domain.Customer;
import com.study.aop006.repository.CustomerRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void addCustomers(List<Customer> customers, int when) {
        for(int i = 0; i < customers.size(); i++) {
            if(i == when) {
                throw new DataAccessException("고객 추가 중 오류가 발생했습니다.") {};
            }
            customerRepository.save(customers.get(i));
        }
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAllByOrderByNoAsc();
    }
}