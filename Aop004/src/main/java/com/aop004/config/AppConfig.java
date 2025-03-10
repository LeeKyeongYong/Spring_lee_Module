package com.aop004.config;

import com.aop004.dao.CustomerDao;
import com.aop004.service.CustomerService;
import com.aop004.service.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import com.aop004.service.TransactionProxyFactoryBean;
@Configuration
public class AppConfig {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public CustomerService customerService() {
        CustomerServiceImpl service = new CustomerServiceImpl(customerDao);
        return service;
    }

    @Bean
    public CustomerService customerServiceTxProxy() throws Exception {
        TransactionProxyFactoryBean factoryBean = new TransactionProxyFactoryBean();
        factoryBean.setTarget(customerService());
        factoryBean.setPlatformTransactionManager(transactionManager);
        factoryBean.setPrefix("addCustomers");
        factoryBean.setServiceInterface(CustomerService.class);
        return (CustomerService) factoryBean.getObject();
    }
}