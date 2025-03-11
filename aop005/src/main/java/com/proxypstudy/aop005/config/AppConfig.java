package com.proxypstudy.aop005.config;

import com.proxypstudy.aop005.dao.CustomerDao;
import com.proxypstudy.aop005.dao.CustomerDaoImpl;
import com.proxypstudy.aop005.service.CustomerService;
import com.proxypstudy.aop005.service.CustomerServiceImpl;
import com.proxypstudy.aop005.service.TransactionAdvice;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class AppConfig {

    @Bean
    public CustomerDao customerDao(DataSource dataSource) {
        CustomerDaoImpl customerDao = new CustomerDaoImpl();
        customerDao.setDataSource(dataSource);
        return customerDao;
    }

    @Bean
    public CustomerServiceImpl customerServiceImpl(CustomerDao customerDao) {
        return new CustomerServiceImpl(customerDao);
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public TransactionAdvice transactionAdvice(PlatformTransactionManager transactionManager) {
        TransactionAdvice advice = new TransactionAdvice();
        advice.setPlatformTransactionManager(transactionManager);
        return advice;
    }

    @Bean
    public NameMatchMethodPointcut transactionPointcut() {
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("addCustomers");
        return pointcut;
    }

    @Bean
    public DefaultPointcutAdvisor transactionAdvisor(TransactionAdvice transactionAdvice, NameMatchMethodPointcut transactionPointcut) {
        return new DefaultPointcutAdvisor(transactionPointcut, transactionAdvice);
    }

    @Bean
    public CustomerService customerServiceTxProxy(CustomerServiceImpl customerServiceImpl, DefaultPointcutAdvisor transactionAdvisor) {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(customerServiceImpl);
        proxyFactoryBean.addAdvisor(transactionAdvisor);
        proxyFactoryBean.setProxyTargetClass(true); // CGLIB 프록시 사용

        return (CustomerService) proxyFactoryBean.getObject();
    }
}